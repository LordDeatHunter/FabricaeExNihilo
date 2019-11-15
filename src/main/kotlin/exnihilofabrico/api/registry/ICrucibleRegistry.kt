package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.FluidStack
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.recipes.CrucibleRecipe
import exnihilofabrico.util.getId
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item

interface ICrucibleRegistry {
    fun clear()
    fun register(recipe: CrucibleRecipe): Boolean

    fun register(input: ItemIngredient, output: FluidStack) =
        register(CrucibleRecipe(input, output))
    fun register(input: ItemIngredient, fluid: Fluid, amount: Int) =
        register(input, FluidStack(fluid.getId(), amount))

    fun getResult(item: Item): FluidStack?
}