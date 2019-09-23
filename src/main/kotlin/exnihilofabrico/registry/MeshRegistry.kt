package exnihilofabrico.registry

import exnihilofabrico.api.registry.IMeshRegistry
import exnihilofabrico.modules.sieves.MeshProperties
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

class MeshRegistry(val registry: MutableList<MeshProperties> = mutableListOf()): IMeshRegistry {
    override fun clear() = registry.clear()
    override fun getAll() = registry
    override fun register(properties: MeshProperties) = registry.add(properties)
    override fun registerItems(itemRegistry: Registry<Item>) =
        registry.forEach { Registry.register(itemRegistry, it.identifier, it.item) }

}