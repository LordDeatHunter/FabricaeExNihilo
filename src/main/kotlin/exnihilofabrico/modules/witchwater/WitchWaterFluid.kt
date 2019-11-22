package exnihilofabrico.modules.witchwater

import exnihilofabrico.modules.ModFluids
import exnihilofabrico.modules.base.AbstractFluid
import exnihilofabrico.modules.base.FluidSettings
import net.minecraft.fluid.Fluid
import net.minecraft.item.BucketItem
import java.util.function.Supplier

class WitchWaterFluid(isStill: Boolean): AbstractFluid(isStill, fluidSettings, Supplier { block }, Supplier { bucket }, Supplier { flowing }, Supplier { still }) {
    override fun matchesType(fluid: Fluid) = fluid == still || fluid == flowing
    companion object {
        val fluidSettings = FluidSettings("witchwater")
        val still = WitchWaterFluid(true)
        val flowing = WitchWaterFluid(false)
        val bucket = BucketItem(still, ModFluids.bucketItemSettings)
        val block = WitchWaterBlock(still, ModFluids.blockSettings)
    }
}
