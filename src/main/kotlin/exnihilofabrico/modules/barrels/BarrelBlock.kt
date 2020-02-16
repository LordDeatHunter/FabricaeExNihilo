package exnihilofabrico.modules.barrels

import alexiil.mc.lib.attributes.AttributeList
import alexiil.mc.lib.attributes.AttributeProvider
import alexiil.mc.lib.attributes.Simulation
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.modules.ModEffects
import exnihilofabrico.modules.barrels.modes.ItemMode
import exnihilofabrico.modules.base.BaseBlock
import exnihilofabrico.modules.base.addEnchantments
import exnihilofabrico.modules.fluids.BloodFluid
import exnihilofabrico.util.asEntity
import exnihilofabrico.util.asStack
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
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
    override fun getRenderType(state: BlockState?) = BlockRenderType.MODEL

    override fun onUse(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?): ActionResult {
        if(world?.isClient != false || pos == null)
            return ActionResult.CONSUME
        val blockEntity = world.getBlockEntity(pos)
        if(blockEntity is BarrelBlockEntity)
            return blockEntity.activate(state, player, hand, hitResult)
        return ActionResult.CONSUME
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

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity?) {
        if(player?.isCreative == false)
            (world.getBlockEntity(pos) as? BarrelBlockEntity)?.let{barrel ->
                val stack = this.asStack()
                stack.addEnchantments(barrel.enchantments)
                world.spawnEntity(stack.asEntity(world, pos))
                (barrel.mode as? ItemMode)?.let { mode ->
                    world.spawnEntity(mode.stack.asEntity(world, pos))
                }
            }
        super.onBreak(world, pos, state, player)
    }

    /**
     * Milking
     */
    override fun onSteppedOn(world: World, pos: BlockPos, entity: Entity) {
        (entity as? LivingEntity)?.let{living ->
            if(ExNihiloFabrico.config.modules.barrels.enableBleeding) {
                (world.getBlockEntity(pos) as? BarrelBlockEntity)?.let { barrel ->
                    val thorns = barrel.enchantments.getEnchantmentLevel(Enchantments.THORNS)
                    if(thorns > 0 && living.damage(DamageSource.CACTUS, thorns.toFloat()/2)) {
                        val volume = FluidVolume.create(BloodFluid.still, (FluidVolume.BUCKET*thorns / living.maximumHealth).toInt())
                        barrel.fluidTransferable.attemptInsertion(volume, Simulation.ACTION)
                    }
                }
            }
            if(entity is PlayerEntity)
                return
            if(!living.hasStatusEffect(ModEffects.MILKED)) {
                val duration = ExNihiloRegistries.BARREL_MILKING.getResult(entity)?.let { (volume, cooldown) ->
                    (world.getBlockEntity(pos) as? BarrelBlockEntity)?.fluidTransferable?.attemptInsertion(volume, Simulation.ACTION)
                    cooldown } ?: 72000
                living.addStatusEffect(StatusEffectInstance(ModEffects.MILKED, duration,1, false, false, false))
            }
        }
    }

    /**
     * BlockEntity functions
     */
    override fun hasBlockEntity() = true
    override fun createBlockEntity(world: BlockView?) = BarrelBlockEntity(isStone = this.material == Material.STONE)

    override fun onPlaced(world: World?, pos: BlockPos?, state: BlockState?, placer: LivingEntity?, itemStack: ItemStack?) {
        val barrel = (world?.getBlockEntity(pos) as? BarrelBlockEntity) ?: return
        EnchantmentHelper.getEnchantments(itemStack).forEach { enchantment, level ->
            barrel.enchantments.setEnchantmentLevel(enchantment, level)
        }
    }

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