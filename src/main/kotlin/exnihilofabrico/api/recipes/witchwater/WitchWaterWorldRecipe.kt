package exnihilofabrico.api.recipes.witchwater

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.WeightedList

data class WitchWaterWorldRecipe(val fluid: FluidIngredient = FluidIngredient.EMPTY,
                                 val results: WeightedList = WeightedList())