package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.FluidIngredient
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.BucketItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient

data class CrucibleHeatRecipe(val blockIngredient: Ingredient?, val fluidIngredient: FluidIngredient?, val value: Int) {
    fun test(block: Block): Boolean {
        return if(block is FluidBlock)
            test(block as FluidBlock)
        else blockIngredient?.test(ItemStack(block)) ?: false
    }
    fun test(state: BlockState) = test(state.block)
    fun test(fluid: Fluid) = fluidIngredient?.test(fluid) ?: false
    fun test(fluid: FluidBlock) = fluidIngredient?.test(fluid) ?: false
    fun test(fluid: FluidState) = fluidIngredient?.test(fluid) ?: false
    fun test(item: Item) = blockIngredient?.test(ItemStack(item)) == true || fluidIngredient?.test(item) == true
    fun test(stack: ItemStack) = blockIngredient?.test(stack) == true || fluidIngredient?.test(stack.item) == true
}