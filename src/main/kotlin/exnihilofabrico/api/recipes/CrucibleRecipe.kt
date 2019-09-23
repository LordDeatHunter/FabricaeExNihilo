package exnihilofabrico.api.recipes

import exnihilofabrico.modules.fluid.FluidInstance
import net.minecraft.recipe.Ingredient

data class CrucibleRecipe(val input: Ingredient, val output: FluidInstance, val stone: Boolean = true)