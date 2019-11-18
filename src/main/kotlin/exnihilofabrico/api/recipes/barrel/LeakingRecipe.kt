package exnihilofabrico.api.recipes.barrel

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import net.minecraft.block.Block
import net.minecraft.block.Blocks

data class LeakingRecipe(val target: ItemIngredient = ItemIngredient.EMPTY,
                         val fluid: FluidIngredient = FluidIngredient.EMPTY,
                         val loss: Int = 0,
                         val result: Block = Blocks.AIR)