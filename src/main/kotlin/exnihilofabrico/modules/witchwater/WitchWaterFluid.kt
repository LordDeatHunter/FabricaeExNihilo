package exnihilofabrico.modules.witchwater

import exnihilofabrico.id
import exnihilofabrico.modules.ModFluids
import exnihilofabrico.modules.base.AbstractFluid
import exnihilofabrico.modules.base.FluidSettings
import net.minecraft.fluid.Fluid
import net.minecraft.item.BucketItem
import java.util.function.Supplier

class WitchWaterFluid(isStill: Boolean): AbstractFluid(isStill, fluidSettings, Supplier { Block }, Supplier { Bucket }, Supplier { Flowing }, Supplier { Still }) {
    override fun matchesType(fluid: Fluid) = fluid == Still || fluid == Flowing
    companion object {
        val fluidSettings =
            FluidSettings(id("block/witchwater_flow"), id("block/witchwater_still"), false)
        val Still = WitchWaterFluid(true)
        val Flowing = WitchWaterFluid(false)
        val Bucket = BucketItem(Still, ModFluids.bucketItemSettings)
        val Block = WitchWaterBlock(Still, ModFluids.blockSettings)
    }
}
