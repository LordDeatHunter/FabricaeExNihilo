package exnihilofabrico.modules.base

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.BaseFluid
import net.minecraft.fluid.FluidState

/**
 * Empty class to get around the "protected" constructor of FluidBlock
 */
open class BaseFluidBlock(fluid: BaseFluid, settings: Settings): FluidBlock(fluid, settings) {
    private val statesByLevel: MutableList<FluidState> = mutableListOf()
    init {
        statesByLevel.add(fluid.getStill(false))
        for(i in 1 until 8)
            statesByLevel.add(fluid.getFlowing(8 - i, false))
    }
    override fun appendProperties(builder: net.minecraft.state.StateFactory.Builder<Block, BlockState>) {
        builder.add(LEVEL)
    }
}