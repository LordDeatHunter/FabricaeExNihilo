package exnihilofabrico.api.registry

import exnihilofabrico.api.recipes.CrucibleRecipe
import exnihilofabrico.modules.fluid.FluidInstance
import exnihilofabrico.util.getID
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.recipe.Ingredient

interface ICrucibleRegistry {
    fun clear()
    fun register(recipe: CrucibleRecipe): Boolean
    fun register(input: Ingredient, output: FluidInstance) = register(CrucibleRecipe(input, output))
    fun register(input: Ingredient, fluid: Fluid, amount: Int) = register(input, FluidInstance(fluid.getID(), amount))
    fun getResult(item: Item): FluidInstance?
}