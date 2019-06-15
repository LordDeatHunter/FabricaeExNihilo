package exnihilofabrico.common.fluids

import exnihilofabrico.ExNihiloFabrico
import net.minecraft.block.*
import net.minecraft.fluid.BaseFluid
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.BucketItem
import net.minecraft.item.Item
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.IWorld
import net.minecraft.world.ViewableWorld

open class ExNihiloFluid(): BaseFluid() {
    open val stillFluid: Fluid = object: ExNihiloFluid() {
        override fun getLevel(fluidState: FluidState?) = 8
        override fun isStill(fluidState: FluidState?) = true
    }
    open val flowingFluid = object: ExNihiloFluid() {
        override fun getLevel(fluidState: FluidState?) = fluidState?.get(LEVEL) ?: 0
        override fun isStill(fluidState: FluidState?) = false
    }
    open val bucket: Item = BucketItem(this, Item.Settings().itemGroup(ExNihiloFabrico.ITEM_GROUP))
    open val block: Block = BaseFluidBlock(this, Block.Settings.of(Material.WATER).noCollision())

    override fun method_15777(fluidState: FluidState?, view: BlockView?, pos: BlockPos?, fluid: Fluid?, direction: Direction?): Boolean {
        return direction == Direction.DOWN && !(fluid == this)
    }

    override fun toBlockState(fluidState: FluidState?): BlockState {
        return block.defaultState.with(FluidBlock.LEVEL, BaseFluid.method_15741(fluidState)) as BlockState
    }

    override fun getLevel(fluidState: FluidState?) = 8
    override fun isStill(fluidState: FluidState?) = false

    override fun getStill() = stillFluid
    override fun getFlowing() = flowingFluid
    override fun getBucketItem() = bucket

    override fun getBlastResistance() = 100.0f

    override fun beforeBreakingBlock(world: IWorld?, pos: BlockPos?, blockState: BlockState?) {
        val blockEntity = if(blockState?.getBlock()?.hasBlockEntity() == true) world?.getBlockEntity(pos) else null
        Block.dropStacks(blockState, world?.world, pos, blockEntity)
    }

    override fun getLevelDecreasePerBlock(world: ViewableWorld?) = 1
    override fun isInfinite() = false
    override fun method_15733(world: ViewableWorld?) = 4

    override fun getRenderLayer() = BlockRenderLayer.TRANSLUCENT
    override fun getTickRate(world: ViewableWorld?) = 5
}