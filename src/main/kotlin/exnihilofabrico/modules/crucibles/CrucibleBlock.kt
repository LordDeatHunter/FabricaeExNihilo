package exnihilofabrico.modules.crucibles

import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder
import exnihilofabrico.modules.base.BaseBlock
import exnihilofabrico.util.VoxelShapeHelper
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.entity.EntityContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class CrucibleBlock(val texture: Identifier, val craftIngredient: Identifier,
                    settings: FabricBlockSettings = FabricBlockSettings.of(Material.WOOD)):
        BaseBlock(settings), BlockEntityProvider {

    override fun activate(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?): Boolean {
        if(world?.isClient != false || pos == null)
            return true
        val blockEntity = world.getBlockEntity(pos)
        if(blockEntity is CrucibleBlockEntity)
            return blockEntity.activate(state, player, hand, hitResult)
        return false
    }

    override fun neighborUpdate(state: BlockState?, world: World?, pos: BlockPos?, block: Block?, fromPos: BlockPos?, boolean_1: Boolean) {
        if(fromPos == null || fromPos != pos?.down())
            return
        val blockEntity = world?.getBlockEntity(pos) ?: return
        if(blockEntity is CrucibleBlockEntity) {
            blockEntity.updateHeat()
        }
    }
    override fun onBlockAdded(state: BlockState?, world: World?, pos: BlockPos?, state2: BlockState?, boolean: Boolean) {
        if(world?.isClient != false || pos == null)
            return
        val crucible = world.getBlockEntity(pos)
        if(crucible is CrucibleBlockEntity)
            crucible.updateHeat()
    }

    override fun getOutlineShape(state: BlockState?, view: BlockView?, pos: BlockPos?, entityContext: EntityContext?) = SHAPE
    override fun getRenderLayer() = BlockRenderLayer.CUTOUT
    override fun getRenderType(state: BlockState?) = BlockRenderType.MODEL

    /**
     * BlockEntity functions
     */
    override fun hasBlockEntity() = true
    override fun createBlockEntity(world: BlockView?) = CrucibleBlockEntity()


    fun generateRecipe(builder: ShapedRecipeBuilder) {
        builder.pattern("x x", "x x", "xxx")
            .ingredientItem('x', craftIngredient)
            .result(Registry.ITEM.getId(asItem()), 1)
    }

    companion object {
        val SUB_SHAPE = arrayOf<VoxelShape>(
                createCuboidShape(0.0, 0.0, 0.0, 3.0, 3.0, 3.0),
                createCuboidShape(0.0, 0.0, 13.0, 3.0, 3.0, 16.0),
                createCuboidShape(13.0, 0.0, 0.0, 16.0, 3.0, 3.0),
                createCuboidShape(13.0,0.0,13.0,16.0,3.0,16.0),
                createCuboidShape(0.0, 3.0, 0.0, 16.0, 16.0, 16.0)
        )
        val SHAPE = VoxelShapeHelper.union(*SUB_SHAPE)
    }
}