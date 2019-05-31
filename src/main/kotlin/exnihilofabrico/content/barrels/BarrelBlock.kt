package exnihilofabrico.content.barrels

import exnihilofabrico.content.base.BaseBlock
import exnihilofabrico.content.base.IHasModel
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.entity.EntityContext
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView

class BarrelBlock(val texture: Identifier,
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
    override fun createBlockEntity(world: BlockView?) = BarrelBlockEntity()

    companion object {
        val SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
    }
}