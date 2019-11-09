package exnihilofabrico.util

import exnihilofabrico.modules.fluid.FluidInstance
import net.minecraft.fluid.Fluid
import net.minecraft.util.registry.Registry

fun getID(fluidInstance: FluidInstance) = fluidInstance.fluid
fun Fluid.getID() = Registry.FLUID.getId(this)