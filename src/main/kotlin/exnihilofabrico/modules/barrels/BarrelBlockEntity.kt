package exnihilofabrico.modules.barrels

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.crafting.EntityStack
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.id
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.barrels.modes.*
import exnihilofabrico.modules.base.BaseBlockEntity
import exnihilofabrico.util.Color
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Tickable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import kotlin.math.ceil
import kotlin.math.min

class BarrelBlockEntity(var mode: BarrelMode = EmptyMode()): BaseBlockEntity(TYPE), Tickable,
    BlockEntityClientSerializable, SidedInventory {

    var tickCounter = world?.random?.nextInt(ExNihiloFabrico.config.modules.barrels.tickRate) ?: 0

    override fun getInvStack(slot: Int): ItemStack {
        return when(mode) {
            is ItemMode -> (mode as ItemMode).stack
            else -> ItemStack.EMPTY
        }
    }

    override fun clear() {
        if(mode is ItemMode)
            mode = EmptyMode()
    }

    override fun setInvStack(slot: Int, stack: ItemStack?) {
        if(stack?.isEmpty != false) {
            clear()
            return
        }

        (mode as? ItemMode)?.let {
            mode = ItemMode(stack.copy())
            markDirtyClient()
            return@setInvStack
        }

        (mode as? EmptyMode)?.let {
            // Check For Compost Recipes
            val recipe = ExNihiloRegistries.BARREL_COMPOST.getRecipe(stack) ?: return@let

            mode = CompostMode(recipe.result, recipe.amount, recipe.color)
            markDirtyClient()
            return@setInvStack
        }

        (mode as? FluidMode)?.let {
            val result = ExNihiloRegistries.BARREL_ALCHEMY.getRecipe(it.fluid, stack) ?: return@let

            if(result.delay != 0) {
                mode = result.product
                spawnEntity(result.toSpawn)
            }
            else {
                AlchemyMode(mode, result.product, result.toSpawn, result.delay)
            }
            markDirtyClient()
            return@setInvStack
        }

        (mode as? CompostMode)?.let {
            val recipe = ExNihiloRegistries.BARREL_COMPOST.getRecipe(stack) ?: return@let

            if(it.amount <= 1 && recipe.result.isItemEqual(it.result)) {

                val amount = min(it.amount + recipe.amount, 1.0)
                val color = Color.average(recipe.color, it.color, recipe.amount / amount)

                mode = CompostMode(recipe.result, amount, color)
                markDirtyClient()
                return@setInvStack
            }
        }
    }

    override fun removeInvStack(slot: Int): ItemStack {
        if(mode is ItemMode) {
            val stack = (mode as ItemMode).stack
            clear()
            markDirtyClient()
            return stack
        }
        return ItemStack.EMPTY
    }

    override fun canPlayerUseInv(player: PlayerEntity?) = false
    override fun getInvSize() = 1

    override fun takeInvStack(slot: Int, amount: Int): ItemStack {
        return (mode as? ItemMode)?.let{
            val stack = it.stack.split(amount)
            if(it.stack.isEmpty) {
                mode = EmptyMode()
                markDirtyClient()
            }
            stack
        } ?: ItemStack.EMPTY
    }

    override fun isInvEmpty() = mode !is ItemMode

    override fun getInvAvailableSlots(direction: Direction?): IntArray {
        return IntArray(1){0}
    }

    override fun canExtractInvStack(slot: Int, stack: ItemStack?, direction: Direction?): Boolean {
        return if(stack?.isEmpty != false)
            false
        else
            (mode as? ItemMode)?.let {it.stack.isItemEqual(stack) && stack.count >= it.stack.count } ?: false
    }

    override fun canInsertInvStack(slot: Int, stack: ItemStack?, direction: Direction?): Boolean {
        return if(stack?.isEmpty != false)
            false
        else
            (mode as? FluidMode)?.let { ExNihiloRegistries.BARREL_ALCHEMY.hasRecipe(it.fluid, stack) } ?: false ||
            (mode as? EmptyMode)?.let { ExNihiloRegistries.BARREL_COMPOST.hasRecipe(stack) } ?: false
    }

    override fun tick() {
        if (tickCounter <= 0) {
            if(leakTick()) return
            if(alchemyTick()) return
            if(compostTick()) return
            //TODO check for nearby block transformations
            //TODO check adjacent fluid changes
        }
        else {
            tickCounter -= 1
        }
    }

    private fun compostTick(): Boolean {
        (mode as? CompostMode)?.let { compostMode ->
            if(compostMode.amount >= 1.0) {
                compostMode.progress += ExNihiloFabrico.config.modules.barrels.compostRate
                markDirtyClient()
            }
            return@compostTick true
        }
        return false
    }

    private fun alchemyTick(): Boolean {
        (mode as? AlchemyMode)?.let { alchemyMode ->
            alchemyMode.countdown -= 1
            if (alchemyMode.countdown <= 0) {
                spawnEntity(alchemyMode.toSpawn)
                mode = alchemyMode.after
            }
            markDirtyClient()
            return@alchemyTick true
        }
        return false
    }

    private fun leakTick(): Boolean {
        (world)?.let { world ->
            (mode as? FluidMode)?.let {fluidMode ->
                if(tickCounter <= 0) {
                    tickCounter = ExNihiloFabrico.config.modules.barrels.tickRate
                    val leakPos = getLeakPos() ?: return@leakTick false
                    val leakResult = ExNihiloRegistries.BARREL_LEAKING.getResult(world.getBlockState(leakPos).block, fluidMode.fluid)
                    if(leakResult != null) {
                        world.setBlockState(leakPos, leakResult.first.defaultState)
                        fluidMode.fluid.amount = leakResult.second.amount
                        markDirtyClient()
                        return@leakTick true
                    }
                }
            }
        }
        return false
    }

    /**
     * NBT Serialization section
     */
    override fun toTag(tag: CompoundTag) = toTagWithoutWorldInfo(super.toTag(tag))

    override fun fromTag(tag: CompoundTag?) {
        super.fromTag(tag)
        if(tag==null){
            ExNihiloFabrico.LOGGER.warn("A barrel at $pos is missing data.")
            return
        }
        fromTagWithoutWorldInfo(tag)
    }

    override fun toClientTag(tag: CompoundTag?) = toTag(tag ?: CompoundTag())
    override fun fromClientTag(tag: CompoundTag?) = fromTag(tag ?: CompoundTag())

    private fun toTagWithoutWorldInfo(tag: CompoundTag): CompoundTag {
        tag.put(mode.tagKey(), mode.toTag())
        return tag
    }

    private fun fromTagWithoutWorldInfo(tag: CompoundTag) {
        mode = barrelModeFactory(tag)
    }

    private fun spawnEntity(entityStack: EntityStack) {
        if(entityStack.isEmpty())
            return
        (world)?.let { world ->
            val entity = entityStack.getEntity(world, pos.up(ceil(entityStack.type.height).toInt())) ?: return
            world.spawnEntity(entity)
            // TODO play some particles
            entityStack.size -= 1
        }
    }

    /**
     * Returns a valid BlockPos or null
     */
    private fun getLeakPos(): BlockPos? {
        val rand = world?.random ?: return null
        val r= 1 + 2 * ExNihiloFabrico.config.modules.barrels.leakRadius
        val leakPos = pos.add(rand.nextInt(2*r)-r, -1, rand.nextInt(2*r)-r)
        if(world?.isHeightValidAndBlockLoaded(leakPos) ?: return null)
            return leakPos
        return null
    }

    companion object {
        val TYPE: BlockEntityType<BarrelBlockEntity> =
            BlockEntityType.Builder.create({BarrelBlockEntity()},
                ModBlocks.BARRELS.values.toTypedArray()).build(null)
        val BLOCK_ENTITY_ID = id("barrel")
    }
}