package exnihilofabrico.common.base

import net.minecraft.block.BlockRenderLayer
import net.minecraft.block.BlockState
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.BaseFluid
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.state.StateFactory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.IWorld
import net.minecraft.world.ViewableWorld
import java.util.function.Supplier

abstract class AbstractFluid(private val still: Boolean,
                             val fluidSettings: FluidSettings,
                             private val fluidBlockSupplier: Supplier<FluidBlock>,
                             private val bucketItemSupplier: Supplier<Item>,
                             private val flowingSupplier: Supplier<BaseFluid>,
                             private val stillSupplier: Supplier<BaseFluid>): BaseFluid() {

    override fun toBlockState(fluidState: FluidState?): BlockState = fluidBlockSupplier.get().defaultState.with(FluidBlock.LEVEL, method_15741(fluidState))

    override fun getLevelDecreasePerBlock(world: ViewableWorld?) = 1
    override fun getBlastResistance() = 100f
    override fun method_15733(world: ViewableWorld?) = 4

    override fun getStill() = stillSupplier.get()
    override fun getFlowing() = flowingSupplier.get()
    override fun getBucketItem() = bucketItemSupplier.get()

    override fun getRenderLayer() = BlockRenderLayer.TRANSLUCENT

    override fun getTickRate(world: ViewableWorld?) = 10
    override fun isInfinite() = fluidSettings.isInfinite
    override fun isStill(fluidState: FluidState?) = still
    override fun getLevel(fluidState: FluidState?) = if(still) 8 else fluidState?.get(LEVEL) ?: 8

    override fun beforeBreakingBlock(world: IWorld?, pos: BlockPos?, state: BlockState?) {

    }
    
    override fun appendProperties(builder: StateFactory.Builder<Fluid, FluidState>?) {
        super.appendProperties(builder)
        if(!still)
            builder?.add(LEVEL)
    }
    
    override fun method_15777(fluidState: FluidState?, view: BlockView?, pos: BlockPos?, fluid: Fluid?, dir: Direction?): Boolean {
        // I have no clue what this function does.
        return false
    }


 }