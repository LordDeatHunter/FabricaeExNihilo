package exnihilofabrico.api.registry

import exnihilofabrico.common.ore.EnumChunkMaterial
import exnihilofabrico.common.ore.EnumChunkShape
import exnihilofabrico.common.ore.EnumPieceShape
import exnihilofabrico.common.ore.OreProperties
import exnihilofabrico.util.Color
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.item.Item
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.util.registry.Registry

interface IOreRegistry {
    fun clear()
    fun register(vararg properties: OreProperties): Boolean
    fun register(material: String, color: Color, pieceShape: EnumPieceShape, chunkShape: EnumChunkShape, chunkMaterial: EnumChunkMaterial): Boolean =
        register(OreProperties(material, color, pieceShape, chunkShape, chunkMaterial))

    fun registerPieceItems(itemRegistry: Registry<Item>)
    fun registerChunkItems(itemRegistry: Registry<Item>)
    fun registerCraftingRecipes()
    fun registerItemTags()

    fun getPropertiesForModel(identifier: ModelIdentifier): OreProperties?
}