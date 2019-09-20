package exnihilofabrico.api.registry

import exnihilofabrico.common.sieves.MeshProperties
import exnihilofabrico.util.Color
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

interface IMeshRegistry {
    fun clear()
    fun getAll(): List<MeshProperties>

    fun register(properties: MeshProperties): Boolean
    fun register(identifer: Identifier, displayName: String, color: Color, ingredient: Identifier) =
        register(MeshProperties(identifer, displayName, color, ingredient))

    fun registerItems(itemRegistry: Registry<Item>)
}