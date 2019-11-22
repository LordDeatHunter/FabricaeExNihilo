package exnihilofabrico.util

import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.ItemStack

fun FluidBlock.getDefaultFluidState(): FluidState = this.getFluidState(this.defaultState)
fun FluidBlock.getFluid(): Fluid = this.getDefaultFluidState().fluid

fun IBucketItem.proxyFluidVolume(stack: ItemStack): FluidVolume {
    return if(this.libblockattributes__getFluid(stack).isEmpty)
        FluidKeys.EMPTY.withAmount(0)
    else
        FluidVolume.create(this.libblockattributes__getFluid(stack), this.libblockattributes__getFluidVolumeAmount())
}

fun FluidVolume.copyLess(amount: Int): FluidVolume {
    return if(amount >= this.amount)
        FluidKeys.EMPTY.withAmount(0)
    else
        FluidVolume.create(this.fluidKey, this.amount - amount)
}