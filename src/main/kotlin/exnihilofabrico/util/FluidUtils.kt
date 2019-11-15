package exnihilofabrico.util

import exnihilofabrico.api.crafting.FluidStack
import exnihilofabrico.impl.BucketFluidAccessor
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item

fun getId(fluidStack: FluidStack) = fluidStack.fluid

fun FluidBlock.getDefaultFluidState(): FluidState = this.getFluidState(this.defaultState)
fun FluidBlock.getFluid(): Fluid = this.getDefaultFluidState().fluid

/**
 * Return the fluid represented by an item, or null if it does not represent a fluid
 */
fun Item.maybeGetFluid(): Fluid? {
    return when(this) {
        is BucketFluidAccessor -> this.getFluid()
        else -> null
    }
}