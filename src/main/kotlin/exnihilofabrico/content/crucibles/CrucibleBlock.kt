package exnihilofabrico.content.crucibles

import exnihilofabrico.content.base.BaseBlock
import exnihilofabrico.content.base.IHasModel
import exnihilofabrico.util.VoxelShapeHelper
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.entity.EntityContext
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

class CrucibleBlock(val texture: Identifier,
                    settings: FabricBlockSettings = FabricBlockSettings.of(Material.WOOD)):
        BaseBlock(settings), BlockEntityProvider, IHasModel {
    override fun getModel(): UnbakedModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOutlineShape(state: BlockState?, view: BlockView?, pos: BlockPos?, entityContext: EntityContext?) = SHAPE
    override fun getRenderLayer() = BlockRenderLayer.CUTOUT
    override fun getRenderType(state: BlockState?) = BlockRenderType.MODEL

    /**
     * BlockEntity functions
     */
    override fun hasBlockEntity() = true
    override fun createBlockEntity(world: BlockView?) = CrucibleBlockEntity()

    companion object {
        val SUB_SHAPE = arrayOf<VoxelShape>(
                Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 3.0, 3.0),
                Block.createCuboidShape(0.0, 0.0, 13.0, 3.0, 3.0, 16.0),
                Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 3.0, 3.0),
                Block.createCuboidShape(13.0,0.0,13.0,16.0,3.0,16.0),
                Block.createCuboidShape(0.0, 3.0, 0.0, 16.0, 16.0, 16.0)
        )
        val SHAPE = VoxelShapeHelper.union(*SUB_SHAPE)
    }
}