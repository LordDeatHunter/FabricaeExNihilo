package exnihilofabrico.api.registry

import exnihilofabrico.common.ore.OreProperties
import exnihilofabrico.common.sieves.MeshProperties
import exnihilofabrico.util.Color
import net.minecraft.item.Item
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

interface IMeshRegistry {
    fun clear()
    fun getAll(): List<MeshProperties>

    fun register(properties: MeshProperties): Boolean
    fun register(identifer: Identifier, displayName: String, color: Color, keyIngredient: Ingredient) =
        register(MeshProperties(identifer, displayName, color, keyIngredient))

    fun registerItems(itemRegistry: Registry<Item>)
}