package exnihilofabrico.api.registry

import exnihilofabrico.api.recipes.CrucibleRecipe
import io.github.prospector.silk.fluid.FluidInstance
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.recipe.Ingredient

interface ICrucibleRegistry {
    fun clear()
    fun register(recipe: CrucibleRecipe)
    fun register(input: Ingredient, output: FluidInstance) = register(CrucibleRecipe(input, output))
    fun register(input: Ingredient, fluid: Fluid, amount: Int) = register(input, FluidInstance(fluid, amount))
    fun getResult(item: Item): FluidInstance?
}