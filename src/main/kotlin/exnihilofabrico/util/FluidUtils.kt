package exnihilofabrico.util

import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import exnihilofabrico.impl.BucketFluidAccessor
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

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

fun IBucketItem.proxyFluidVolume(stack: ItemStack): FluidVolume {
    return if(this.libblockattributes__getFluid(stack).isEmpty)
        FluidKeys.EMPTY.withAmount(0)
    else
        FluidVolume.create(this.libblockattributes__getFluid(stack), this.libblockattributes__getFluidVolumeAmount())
}