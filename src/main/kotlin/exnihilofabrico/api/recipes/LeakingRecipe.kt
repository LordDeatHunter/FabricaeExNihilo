package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import net.minecraft.block.Block

data class LeakingRecipe(val target: ItemIngredient,
                         val fluid: FluidIngredient,
                         val loss: Int,
                         val result: Block)