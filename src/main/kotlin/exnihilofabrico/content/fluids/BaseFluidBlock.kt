package exnihilofabrico.content.fluids

import net.minecraft.block.FluidBlock
import net.minecraft.fluid.BaseFluid

/**
 * Empty class to get around the "protected" constructor of FluidBlock
 */
open class BaseFluidBlock(fluid: BaseFluid, settings: Settings): FluidBlock(fluid, settings) {

}