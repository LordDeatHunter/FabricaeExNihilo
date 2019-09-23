package exnihilofabrico.modules.sieves

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
import virtuoel.towelette.api.Fluidloggable

class SieveBlock(val texture: Identifier,
                 val craftIngredient1: Identifier, val craftIngredient2: Identifier,
                 settings: FabricBlockSettings = FabricBlockSettings.of(Material.WOOD)):
        BaseBlock(settings), BlockEntityProvider, Fluidloggable {

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


    fun generateRecipe(builder: ShapedRecipeBuilder) {
        builder.pattern("x x", "xyx", "z z")
            .ingredientItem('x', craftIngredient1)
            .ingredientItem('y', craftIngredient2)
            .ingredientItem('z', Identifier("stick"))
            .result(Registry.ITEM.getId(asItem()), 1)
    }

    companion object {
        val SUB_SHAPE = arrayOf<VoxelShape>(
                createCuboidShape(0.0, 0.0, 0.0, 2.0, 12.0, 2.0),
                createCuboidShape(14.0, 0.0, 0.0, 16.0, 12.0, 2.0),
                createCuboidShape(0.0, 0.0, 14.0, 2.0, 12.0, 16.0),
                createCuboidShape(14.0, 0.0, 14.0, 16.0, 12.0, 16.0),
                createCuboidShape(0.0, 8.0, 0.0, 16.0, 12.0, 16.0)
        )
        val SHAPE = VoxelShapeHelper.union(*SUB_SHAPE)
    }
}