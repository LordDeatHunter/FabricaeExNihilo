package exnihilofabrico.registry

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.IOreRegistry
import exnihilofabrico.common.ore.OreChunkItem
import exnihilofabrico.common.ore.OrePieceItem
import exnihilofabrico.common.ore.OreProperties
import exnihilofabrico.util.asStack
import io.github.cottonmc.cotton.datapack.tags.TagEntryManager
import io.github.cottonmc.cotton.datapack.tags.TagType
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.item.Item
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.ShapedRecipe
import net.minecraft.util.DefaultedList
import net.minecraft.util.registry.Registry

data class OreRegistry(val registry: MutableList<OreProperties> = mutableListOf()): IOreRegistry {

    val item_settings: Item.Settings = Item.Settings().group(ExNihiloFabrico.ITEM_GROUP).maxCount(64)

    override fun clear() = registry.clear()
    override fun register(vararg properties: OreProperties) = registry.addAll(properties)

    override fun registerPieceItems(itemRegistry: Registry<Item>) =
        registry.forEach { Registry.register(itemRegistry, it.getPieceID(), OrePieceItem(it, item_settings)) }

    override fun registerChunkItems(itemRegistry: Registry<Item>) =
        registry.forEach { Registry.register(itemRegistry, it.getChunkID(), OreChunkItem(it, item_settings)) }

    override fun registerCraftingRecipes() {
        for(property in registry){
            val piece = Ingredient.ofItems(property.getChunkItem())
            val chunk = property.getPieceItem().asStack()

            val recipe = ShapedRecipe(property.getChunkID(), ExNihiloFabrico.ITEM_GROUP.toString(), 2, 2,
                DefaultedList.copyOf(piece, piece, piece, piece), chunk)

        }
    }

    override fun registerItemTags() {
        registry.forEach {property ->
            property.getChunkTags().forEach {tag ->
                TagEntryManager.registerToTag(TagType.ITEM, tag, property.getChunkID().toString())
            }
            property.getPieceTags().forEach {tag ->
                TagEntryManager.registerToTag(TagType.ITEM, tag, property.getPieceID().toString())
            }
        }
    }

    override fun getPropertiesForModel(identifier: ModelIdentifier): OreProperties? =
        registry.firstOrNull { it.getChunkID().path == identifier.path || it.getPieceID().path == identifier.path }
}