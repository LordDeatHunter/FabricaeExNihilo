package exnihilofabrico.api.recipes

import io.github.prospector.silk.fluid.FluidInstance
import net.minecraft.recipe.Ingredient

data class CrucibleRecipe(val input: Ingredient, val output: FluidInstance)