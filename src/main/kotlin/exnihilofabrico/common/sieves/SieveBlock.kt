package exnihilofabrico.common.sieves

import exnihilofabrico.common.base.BaseBlock
import exnihilofabrico.common.base.IHasModel
import exnihilofabrico.util.VoxelShapeHelper
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.entity.EntityContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
//import virtuoel.towelette.api.Fluidloggable

class SieveBlock(val texture: Identifier,
                 settings: FabricBlockSettings = FabricBlockSettings.of(Material.WOOD)):
        BaseBlock(settings), BlockEntityProvider, IHasModel/*, Fluidloggable*/ {
    override fun getModel(): UnbakedModel {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOutlineShape(state: BlockState?, view: BlockView?, pos: BlockPos?, entityContext: EntityContext?) = SHAPE
    override fun getRenderLayer() = BlockRenderLayer.CUTOUT
    override fun getRenderType(state: BlockState?) = BlockRenderType.MODEL

    override fun activate(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?): Boolean {
        if(world?.isClient != false || pos == null)
            return true
        val blockEntity = world.getBlockEntity(pos)
        if(blockEntity is SieveBlockEntity)
            return blockEntity.activate(state, player, hand, hitResult)
        return false
    }

    /**
     * BlockEntity functions
     */
    override fun hasBlockEntity() = true
    override fun createBlockEntity(world: BlockView?) = SieveBlockEntity()

    companion object {
        val SUB_SHAPE = arrayOf<VoxelShape>(
                Block.createCuboidShape(0.0, 0.0, 0.0, 2.0, 12.0, 2.0),
                Block.createCuboidShape(14.0, 0.0, 0.0, 16.0, 12.0, 2.0),
                Block.createCuboidShape(0.0, 0.0, 14.0, 2.0, 12.0, 16.0),
                Block.createCuboidShape(14.0, 0.0, 14.0, 16.0, 12.0, 16.0),
                Block.createCuboidShape(0.0, 8.0, 0.0, 16.0, 12.0, 16.0)
        )
        val SHAPE = VoxelShapeHelper.union(*SUB_SHAPE)
    }
}