package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.WeightedList

data class WitchWaterWorldRecipe(val fluid: FluidIngredient,
                                 val results: WeightedList
)