package exnihilofabrico.api.recipes.crucible

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import exnihilofabrico.api.crafting.ItemIngredient

data class CrucibleRecipe(val input: ItemIngredient, val output: FluidVolume)