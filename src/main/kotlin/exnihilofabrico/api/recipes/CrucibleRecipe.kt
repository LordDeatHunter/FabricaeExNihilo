package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.FluidStack
import exnihilofabrico.api.crafting.ItemIngredient

data class CrucibleRecipe(val input: ItemIngredient,
                          val output: FluidStack)