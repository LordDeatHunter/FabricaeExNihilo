package exnihilofabrico.api.registry

import exnihilofabrico.modules.sieves.MeshProperties
import exnihilofabrico.util.Color
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

interface IMeshRegistry {
    fun clear()
    fun getAll(): List<MeshProperties>

    fun register(properties: MeshProperties): Boolean
    fun register(identifier: Identifier, enchantability: Int, displayName: String, color: Color, ingredient: Identifier) =
        register(MeshProperties(identifier, enchantability, displayName, color, ingredient))

    fun registerItems(itemRegistry: Registry<Item>)
}