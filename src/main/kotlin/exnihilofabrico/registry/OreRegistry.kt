package exnihilofabrico.registry

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.IOreRegistry
import exnihilofabrico.modules.ore.OreChunkItem
import exnihilofabrico.modules.ore.OrePieceItem
import exnihilofabrico.modules.ore.OreProperties
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

data class OreRegistry(val registry: MutableList<OreProperties> = mutableListOf()): IOreRegistry {

    val item_settings: Item.Settings = Item.Settings().group(ExNihiloFabrico.ITEM_GROUP).maxCount(64)

    override fun clear() = registry.clear()
    override fun getAll() = registry
    override fun register(vararg properties: OreProperties) = registry.addAll(properties)

    override fun registerPieceItems(itemRegistry: Registry<Item>) =
        registry.forEach { Registry.register(itemRegistry, it.getPieceID(), OrePieceItem(it, item_settings)) }

    override fun registerChunkItems(itemRegistry: Registry<Item>) =
        registry.forEach { Registry.register(itemRegistry, it.getChunkID(), OreChunkItem(it, item_settings)) }

    override fun getPropertiesForModel(identifier: ModelIdentifier): OreProperties? =
        registry.firstOrNull { it.getChunkID().path == identifier.path || it.getPieceID().path == identifier.path }
}