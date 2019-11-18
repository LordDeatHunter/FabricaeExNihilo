package exnihilofabrico.api.recipes.crucible

import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

data class CrucibleHeatRecipe(val ingredient: ItemIngredient, val fluid: FluidIngredient, val value: Int) {
    fun test(block: Block): Boolean {
        return if(block is FluidBlock)
            test(block)
        else
            ingredient.test(block)
    }
    fun test(state: BlockState) = test(state.block)
    fun test(fluid: Fluid) = this.fluid.test(fluid)
    fun test(fluid: FluidBlock) = this.fluid.test(fluid)
    fun test(fluid: FluidState) = this.fluid.test(fluid)
    fun test(item: Item) = ingredient.test(item)
    fun test(stack: ItemStack) = test(stack.item)
}