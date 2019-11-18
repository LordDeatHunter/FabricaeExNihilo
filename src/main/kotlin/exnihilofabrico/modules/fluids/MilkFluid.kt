package exnihilofabrico.modules.fluids

import exnihilofabrico.id
import exnihilofabrico.modules.ModFluids
import exnihilofabrico.modules.base.AbstractFluid
import exnihilofabrico.modules.base.FluidSettings
import net.minecraft.fluid.Fluid
import net.minecraft.item.Items
import java.util.function.Supplier

class MilkFluid(isStill: Boolean): AbstractFluid(isStill, fluidSettings, Supplier { Block }, Supplier { Bucket }, Supplier { Flowing }, Supplier { Still }) {
    override fun matchesType(fluid: Fluid) = fluid == Still || fluid == Flowing
    companion object {
        val fluidSettings = FluidSettings(id("block/milk_flow"), id("block/milk_still"), false)
        val Still = MilkFluid(true)
        val Flowing = MilkFluid(false)
        val Bucket = Items.MILK_BUCKET
        val Block = MilkBlock(Still, ModFluids.blockSettings)
    }
}