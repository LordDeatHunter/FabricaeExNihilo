package exnihilofabrico.modules.sieves

import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder
import exnihilofabrico.modules.base.BaseBlock
import exnihilofabrico.util.VoxelShapeHelper
import exnihilofabrico.util.asEntity
import exnihilofabrico.util.asStack
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.client.render.RenderLayer
import net.minecraft.entity.EntityContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
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

    init {
        BlockRenderLayerMap.INSTANCE.putBlock(this, RenderLayer.getCutout())
    }

    override fun getOutlineShape(state: BlockState?, view: BlockView?, pos: BlockPos?, entityContext: EntityContext?) = SHAPE
    override fun getRenderType(state: BlockState?) = BlockRenderType.MODEL

    override fun onUse(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?): ActionResult {
        if(world?.isClient != false || pos == null)
            return ActionResult.PASS
        val blockEntity = world.getBlockEntity(pos)
        if(blockEntity is SieveBlockEntity)
            return blockEntity.activate(state, player, hand, hitResult)
        return ActionResult.PASS
    }

    /**
     * BlockEntity functions
     */
    override fun hasBlockEntity() = true
    override fun createBlockEntity(world: BlockView?) = SieveBlockEntity()

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity?) {
        if (player?.isCreative == false)
            (world.getBlockEntity(pos) as? SieveBlockEntity)?.let { sieve ->
                val stack = this.asStack()
                world.spawnEntity(stack.asEntity(world, pos))
                if (!sieve.mesh.isEmpty)
                    world.spawnEntity(sieve.mesh.asEntity(world, pos))
                if (!sieve.contents.isEmpty)
                    world.spawnEntity(sieve.contents.asEntity(world, pos))
            }
        super.onBreak(world, pos, state, player)
    }

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