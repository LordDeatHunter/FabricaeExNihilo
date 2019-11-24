package exnihilofabrico.api.recipes.barrel

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.modules.barrels.modes.BarrelMode

data class FluidTransformRecipe(val inBarrel: FluidIngredient, val catalyst: ItemIngredient, val result: BarrelMode)