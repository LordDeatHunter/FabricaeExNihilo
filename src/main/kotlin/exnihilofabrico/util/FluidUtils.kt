package exnihilofabrico.util

import exnihilofabrico.modules.fluid.FluidInstance
import net.minecraft.fluid.Fluid
import net.minecraft.util.registry.Registry

fun getFluidID(fluidInstance: FluidInstance) = fluidInstance.fluid
fun getFluidID(fluid: Fluid) = Registry.FLUID.getId(fluid)