package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.EntityTypeIngredient
import exnihilofabrico.api.crafting.FluidStack

data class MilkingRecipe(val entity: EntityTypeIngredient,
                         val result: FluidStack,
                         val cooldown: Int)