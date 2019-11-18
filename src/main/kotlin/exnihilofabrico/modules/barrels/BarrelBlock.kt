package exnihilofabrico.modules.barrels

import alexiil.mc.lib.attributes.AttributeList
import alexiil.mc.lib.attributes.AttributeProvider
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder
import exnihilofabrico.modules.base.BaseBlock
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.entity.EntityContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.IWorld
import net.minecraft.world.World

class BarrelBlock(val texture: Identifier,
                  val craftIngredient1: Identifier, val craftIngredient2: Identifier,
                  settings: FabricBlockSettings = FabricBlockSettings.of(Material.WOOD)):
        BaseBlock(settings), BlockEntityProvider, AttributeProvider, InventoryProvider {

    override fun getOutlineShape(state: BlockState?, view: BlockView?, pos: BlockPos?, entityContext: EntityContext?) = SHAPE
    override fun getRenderLayer() = BlockRenderLayer.CUTOUT
    override fun getRenderType(state: BlockState?) = BlockRenderType.MODEL

    override fun activate(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?): Boolean {
        if(world?.isClient != false || pos == null)
            return true
        val blockEntity = world.getBlockEntity(pos)
        if(blockEntity is BarrelBlockEntity)
            return blockEntity.activate(state, player, hand, hitResult)
        return false
    }
    override fun addAllAttributes(world: World, pos: BlockPos, state: BlockState, attributes: AttributeList<*>) {
        val blockEntity = world.getBlockEntity(pos)
        if(blockEntity is BarrelBlockEntity) {
            attributes.offer(blockEntity.itemTransferable)
            attributes.offer(blockEntity.fluidTransferable)
        }
    }

    override fun getInventory(state: BlockState?, world: IWorld?, pos: BlockPos): SidedInventory? {
        return (world?.getBlockEntity(pos) as? BarrelBlockEntity)?.inventory
    }

    /**
     * BlockEntity functions
     */
    override fun hasBlockEntity() = true
    override fun createBlockEntity(world: BlockView?) = BarrelBlockEntity(isStone = this.material == Material.STONE)

    fun generateRecipe(builder: ShapedRecipeBuilder) {
        builder.pattern("x x", "x x", "xyx")
            .ingredientItem('x', craftIngredient1)
            .ingredientItem('y', craftIngredient2)
            .result(Registry.ITEM.getId(asItem()), 1)
    }

    companion object {
        val SHAPE: VoxelShape = createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
    }
}