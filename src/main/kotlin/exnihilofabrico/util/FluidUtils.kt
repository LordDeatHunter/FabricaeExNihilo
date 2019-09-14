package exnihilofabrico.util

import io.github.prospector.silk.fluid.FluidInstance
import net.minecraft.fluid.Fluid
import net.minecraft.util.registry.Registry

fun getFluidID(fluidInstance: FluidInstance) = getFluidID(fluidInstance.fluid)
fun getFluidID(fluid: Fluid) = Registry.FLUID.getId(fluid)