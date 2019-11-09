package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.TagIngredient
import exnihilofabrico.api.crafting.test
import exnihilofabrico.util.maybeGetFluid
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

data class CrucibleHeatRecipe(val ingredient: TagIngredient<Item>?, val fluid: TagIngredient<Fluid>?, val value: Int) {
    fun test(block: Block): Boolean {
        return if(block is FluidBlock)
            test(block)
        else
            ingredient?.test(block) ?: false
    }
    fun test(state: BlockState) = test(state.block)
    fun test(fluid: Fluid) = this.fluid?.test(fluid) ?: false
    fun test(fluid: FluidBlock) = this.fluid?.test(fluid) ?: false
    fun test(fluid: FluidState) = this.fluid?.test(fluid) ?: false
    fun test(item: Item) = ingredient?.test(item) == true || fluid?.test(item.maybeGetFluid() ?: Fluids.EMPTY) == true
    fun test(stack: ItemStack) = test(stack.item)
}