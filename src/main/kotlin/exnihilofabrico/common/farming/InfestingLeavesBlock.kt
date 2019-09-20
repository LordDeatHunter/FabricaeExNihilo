package exnihilofabrico.common.farming

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.LeavesBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView

class InfestingLeavesBlock(settings: FabricBlockSettings): LeavesBlock(settings.build()), BlockEntityProvider {

    override fun getRenderType(state: BlockState?) = BlockRenderType.INVISIBLE
    override fun getLightSubtracted(state: BlockState, view: BlockView, pos: BlockPos) = 1

    /**
     * BlockEntity functions
     */
    override fun hasBlockEntity() = true
    override fun createBlockEntity(view: BlockView?) = InfestingLeavesBlockEntity()

}