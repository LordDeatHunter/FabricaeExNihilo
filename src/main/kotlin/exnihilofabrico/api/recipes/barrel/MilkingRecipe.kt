package exnihilofabrico.api.recipes.barrel

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import exnihilofabrico.api.crafting.EntityTypeIngredient

data class MilkingRecipe(val entity: EntityTypeIngredient, val result: FluidVolume, val cooldown: Int)