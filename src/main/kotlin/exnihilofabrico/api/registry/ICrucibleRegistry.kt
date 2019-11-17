package exnihilofabrico.api.registry

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.recipes.CrucibleRecipe
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item

interface ICrucibleRegistry {
    fun clear()
    fun register(recipe: CrucibleRecipe): Boolean

    fun register(input: ItemIngredient, output: FluidVolume) =
        register(CrucibleRecipe(input, output))
    fun register(input: ItemIngredient, fluid: Fluid, amount: Int) =
        register(input, FluidVolume.create(fluid, amount))

    fun getResult(item: Item): FluidVolume?
}