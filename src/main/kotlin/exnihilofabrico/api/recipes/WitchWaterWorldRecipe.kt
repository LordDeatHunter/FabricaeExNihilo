package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.registry.WeightedList

data class WitchWaterWorldRecipe(val fluid: FluidIngredient,
                                 val results: WeightedList
)