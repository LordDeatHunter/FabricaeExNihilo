package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.FluidStack
import exnihilofabrico.api.recipes.CrucibleRecipe
import exnihilofabrico.util.getID
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.recipe.Ingredient

interface ICrucibleRegistry {
    fun clear()
    fun register(recipe: CrucibleRecipe): Boolean
    fun register(input: Ingredient, output: FluidStack) = register(CrucibleRecipe(input, output))
    fun register(input: Ingredient, fluid: Fluid, amount: Int) = register(input,
        FluidStack(fluid.getID(), amount)
    )
    fun getResult(item: Item): FluidStack?
}