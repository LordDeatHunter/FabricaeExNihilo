package exnihilofabrico.api.recipes

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import exnihilofabrico.api.crafting.ItemIngredient

data class CrucibleRecipe(val input: ItemIngredient, val output: FluidVolume)