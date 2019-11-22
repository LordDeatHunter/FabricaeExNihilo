package exnihilofabrico.modules.barrels

import alexiil.mc.lib.attributes.Simulation
import alexiil.mc.lib.attributes.fluid.FluidTransferable
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter
import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import alexiil.mc.lib.attributes.item.ItemTransferable
import alexiil.mc.lib.attributes.item.filter.ExactItemStackFilter
import alexiil.mc.lib.attributes.item.filter.ItemFilter
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.crafting.EntityStack
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.id
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.barrels.modes.*
import exnihilofabrico.modules.base.BaseBlockEntity
import exnihilofabrico.modules.base.EnchantmentContainer
import exnihilofabrico.util.copyLess
import exnihilofabrico.util.ofSize
import exnihilofabrico.util.proxyFluidVolume
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Hand
import net.minecraft.util.Tickable
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import kotlin.math.ceil

class BarrelBlockEntity(var mode: BarrelMode = EmptyMode(), val isStone: Boolean = false): BaseBlockEntity(TYPE), Tickable,
    BlockEntityClientSerializable {

    var tickCounter = world?.random?.nextInt(ExNihiloFabrico.config.modules.barrels.tickRate) ?: ExNihiloFabrico.config.modules.barrels.tickRate

    /**
     * Inventories
     */
    val fluidTransferable = FluidTransferer(this)
    val itemTransferable = ItemTransferer(this)
    val inventory = BarrelInventory(this)

    /**
     * Enchantments
     */
    val enchantments = EnchantmentContainer()

    override fun tick() {
        if (tickCounter <= 0) {
            tickCounter = ExNihiloFabrico.config.modules.barrels.tickRate
            markDirty()
            if(leakTick()) return
            if(alchemyTick()) return
            if(compostTick()) return
            //TODO check for nearby block transformations
            //TODO check adjacent fluid changes
        }
        else {
            tickCounter -= 1
            markDirty()
        }
    }

    private fun getEfficiencyMultiplier() = (1 + enchantments.getEnchantmentLevel(Enchantments.EFFICIENCY))

    private fun compostTick(): Boolean {
        (mode as? CompostMode)?.let { compostMode ->
            if(compostMode.progress >= 1.0) {
                mode = ItemMode(compostMode.result.copy())
                return@compostTick true
            }
            if(compostMode.amount >= 1.0) {
                compostMode.progress += ExNihiloFabrico.config.modules.barrels.compostRate * getEfficiencyMultiplier()
                markDirtyClient()
            }
            return@compostTick true
        }
        return false
    }

    private fun alchemyTick(): Boolean {
        (mode as? AlchemyMode)?.let { alchemyMode ->
            alchemyMode.countdown -= getEfficiencyMultiplier()
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
        if(isStone)
            return false
        (world)?.let { world ->
            if(world.isClient)
                return@leakTick true
            (mode as? FluidMode)?.let {fluidMode ->
                val leakPos = getLeakPos() ?: return@leakTick false
                val leakResult = ExNihiloRegistries.BARREL_LEAKING.getResult(world.getBlockState(leakPos).block, fluidMode.fluid) ?: return@leakTick false

                world.setBlockState(leakPos, leakResult.first.defaultState)
                if(fluidMode.fluid.amount > leakResult.second)
                    fluidMode.fluid.split(leakResult.second)
                else
                    mode = EmptyMode()
                markDirtyClient()
                return@leakTick true
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
        tag.put("enchantments", enchantments.toTag())
        return tag
    }

    private fun fromTagWithoutWorldInfo(tag: CompoundTag) {
        mode = barrelModeFactory(tag)
        if(tag.containsKey("enchantments")) {
            val readEnchantments = EnchantmentContainer()
            readEnchantments.fromTag(tag.getCompound("enchantments"))
            enchantments.setAllEnchantments(readEnchantments)
        }

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
        val r= ExNihiloFabrico.config.modules.barrels.leakRadius
        val leakPos = pos.add(rand.nextInt(2*r)-r, -rand.nextInt(2), rand.nextInt(2*r)-r)
        if(world?.isHeightValidAndBlockLoaded(leakPos) ?: return null)
            return leakPos
        return null
    }

    fun activate(state: BlockState?, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?): Boolean {
        if(world?.isClient != false || player == null || hand == null)
            return true

        return when(mode) {
            is ItemMode -> { dropInventoryAtPlayer(player); true }
            is EmptyMode, is CompostMode, is FluidMode -> insertFromHand(player, hand)
            else -> false
        }
    }

    fun dropInventoryAtPlayer(player: PlayerEntity) {
        (mode as? ItemMode)?.let {
            val entity = ItemEntity(world, pos.x.toDouble() + 0.5, pos.y.toDouble() + 1.0625, pos.z.toDouble() + 0.5, it.stack)
            entity.velocity = player.pos.subtract(entity.pos).normalize().multiply(0.5)
            world?.spawnEntity(entity)
            mode = EmptyMode()
            markDirtyClient()
        }
    }

    fun insertFromHand(player: PlayerEntity, hand: Hand): Boolean {
        val held = player.getStackInHand(hand) ?: ItemStack.EMPTY
        val remaining = itemTransferable.attemptInsertion(held, Simulation.ACTION)
        if(remaining.count != held.count) {
            held.decrement(1)
            return true
        }
        // Check for fluids
        (held.item as? IBucketItem)?.let { bucket ->
            // Filling with a fluid
            val volume = bucket.proxyFluidVolume(held)
            val amount = bucket.libblockattributes__getFluidVolumeAmount()
            if(!volume.isEmpty) {
                val remaining = fluidTransferable.attemptInsertion(volume, Simulation.ACTION)
                if(remaining.isEmpty) {
                    val returnStack = bucket.libblockattributes__drainedOfFluid(held)
                    if(!player.isCreative)
                        held.decrement(1)
                    player.giveItemStack(returnStack)
                    markDirtyClient()
                    return@insertFromHand true
                }
            }
            else {
                (mode as? FluidMode)?.let { fluidMode ->
                    // Removing a bucket's worth of fluid
                    val drained = fluidTransferable.attemptExtraction({ true }, amount, Simulation.SIMULATE)
                    if(drained.amount == bucket.libblockattributes__getFluidVolumeAmount()) {
                        val returnStack = bucket.libblockattributes__withFluid(fluidMode.fluid.fluidKey)
                        fluidTransferable.attemptExtraction({ true }, amount, Simulation.ACTION)
                        if(!player.isCreative)
                            held.decrement(1)
                        player.giveItemStack(returnStack)
                        markDirtyClient()
                        return@insertFromHand true
                    }
                }
            }
            false
        }
        return false
    }

    /**
     * Fluid Inventory Management
     */
    class FluidTransferer(val barrel: BarrelBlockEntity): FluidTransferable {
        override fun attemptInsertion(volume: FluidVolume, simulation: Simulation): FluidVolume {
            (barrel.mode as? EmptyMode)?.let { emptyMode ->
                val amount = minOf(FluidVolume.BUCKET, volume.amount)
                if(amount > 0) {
                    val toTake = FluidVolume.create(volume.fluidKey, amount)
                    if(simulation.isAction) {
                        barrel.mode = FluidMode(toTake)
                        barrel.markDirtyClient()
                    }
                    return@attemptInsertion volume.copyLess(toTake.amount)
                }
            }
            (barrel.mode as? FluidMode)?.let { fluidMode ->
                if(fluidMode.fluid.canMerge(volume)) {
                    val amount = minOf(FluidVolume.BUCKET - fluidMode.fluid.amount, volume.amount)
                    if(amount > 0) {
                        val toTake = FluidVolume.create(fluidMode.fluid.fluidKey, amount)
                        if(simulation.isAction) {
                            fluidMode.fluid.merge(toTake, Simulation.ACTION)
                            barrel.markDirtyClient()
                        }
                        return@attemptInsertion volume.copyLess(toTake.amount)
                    }
                }
            }

            return volume
        }

        override fun attemptExtraction(filter: FluidFilter, amount: Int, simulation: Simulation): FluidVolume {
            return (barrel.mode as? FluidMode)?.let { fluidMode ->
                if(filter.matches(fluidMode.fluid.fluidKey)) {
                    val returnVolume = FluidVolume.create(fluidMode.fluid.fluidKey, minOf(fluidMode.fluid.amount, amount))
                    if(simulation.isAction) {
                        if(amount >= fluidMode.fluid.amount)
                            barrel.mode = EmptyMode()
                        else
                            fluidMode.fluid.split(amount)
                        barrel.markDirtyClient()
                    }
                    returnVolume
                }
                else {
                    FluidKeys.EMPTY.withAmount(0)
                }
            } ?: FluidKeys.EMPTY.withAmount(0)
        }
    }

    /**
     * Item Inventory Management
     */
    class ItemTransferer(val barrel: BarrelBlockEntity): ItemTransferable {
        override fun attemptInsertion(stack: ItemStack, simulation: Simulation): ItemStack {
            (barrel.mode as? EmptyMode)?.let { emptyMode ->
                val recipe = ExNihiloRegistries.BARREL_COMPOST.getRecipe(stack) ?: return@attemptInsertion stack
                if(simulation.isAction) {
                    barrel.mode = CompostMode(recipe.result, recipe.amount, recipe.color)
                    barrel. markDirtyClient()
                }
                return@attemptInsertion stack.ofSize(stack.count-1)

            }
            (barrel.mode as? FluidMode)?.let { fluidMode ->
                val result = ExNihiloRegistries.BARREL_ALCHEMY.getRecipe(fluidMode.fluid, stack) ?: return@attemptInsertion stack

                if(simulation.isAction) {
                    if(result.delay == 0) {
                        barrel.mode = result.product
                        barrel.spawnEntity(result.toSpawn)
                        barrel.markDirtyClient()
                    }
                    else {
                        barrel.mode = AlchemyMode(fluidMode, result.product, result.toSpawn, result.delay)
                        barrel.markDirtyClient()
                    }
                }
                barrel.markDirtyClient()
                return@attemptInsertion  stack.ofSize(stack.count-1)
            }
            (barrel.mode as? CompostMode)?.let { compostMode ->
                val recipe = ExNihiloRegistries.BARREL_COMPOST.getRecipe(stack) ?: return@attemptInsertion stack

                if(compostMode.amount < 1.0 && recipe.result.isItemEqual(compostMode.result)) {
                    if(simulation.isAction) {
                        compostMode.amount = minOf(compostMode.amount + recipe.amount, 1.0)
                        compostMode.color = recipe.color
                        compostMode.progress = 0.0
                        barrel.markDirtyClient()
                    }

                    return@attemptInsertion  stack.ofSize(stack.count-1)
                }
            }
            return stack
        }

        override fun attemptExtraction(filter: ItemFilter, amount: Int, simulation: Simulation): ItemStack {
            return (barrel.mode as? ItemMode)?.let { itemMode ->
                if(filter.matches(itemMode.stack)) {
                    val returnStack = itemMode.stack.ofSize(minOf(amount, itemMode.stack.count))
                    if(simulation.isAction) {
                        if(amount >= itemMode.stack.count)
                            barrel.mode = EmptyMode()
                        else
                            itemMode.stack.decrement(amount)
                        barrel.markDirtyClient()
                    }
                    returnStack
                }
                else {
                    ItemStack.EMPTY
                }
            } ?: ItemStack.EMPTY
        }
    }

    class BarrelInventory(val barrel: BarrelBlockEntity): SidedInventory {
        override fun getInvStack(slot: Int): ItemStack {
            return (barrel.mode as? ItemMode)?.stack ?: ItemStack.EMPTY
        }

        override fun markDirty() = barrel.markDirtyClient()

        override fun clear() {
            (barrel.mode as? ItemMode)?.let { barrel.mode = EmptyMode() }
        }

        override fun setInvStack(slot: Int, stack: ItemStack) {
            if(!stack.isEmpty)
                barrel.itemTransferable.attemptInsertion(stack, Simulation.ACTION)
        }

        override fun removeInvStack(slot: Int): ItemStack {
            return (barrel.mode as? ItemMode)?.let { itemMode ->
                val stack = itemMode.stack
                barrel.mode = EmptyMode()
                barrel.markDirtyClient()
                stack
            } ?: ItemStack.EMPTY
        }

        override fun canPlayerUseInv(p0: PlayerEntity) = false

        override fun getInvAvailableSlots(p0: Direction?) = IntArray(1) {0}

        override fun getInvSize() = 1

        override fun canExtractInvStack(slot: Int, stack: ItemStack, direction: Direction?): Boolean {
            return !barrel.itemTransferable.attemptExtraction(ExactItemStackFilter(stack), stack.count, Simulation.SIMULATE).isEmpty
        }

        override fun takeInvStack(slot: Int, amount: Int): ItemStack {
            return barrel.itemTransferable.attemptExtraction({ _: ItemStack -> true }, amount, Simulation.ACTION)
        }

        override fun isInvEmpty() = barrel.mode !is ItemMode

        override fun canInsertInvStack(slot: Int, stack: ItemStack?, p2: Direction?): Boolean {
            if(stack == null)
                return false
            return (barrel.itemTransferable.attemptInsertion(stack, Simulation.SIMULATE).count != stack.count)
        }

    }

    companion object {
        val TYPE: BlockEntityType<BarrelBlockEntity> =
            BlockEntityType.Builder.create({BarrelBlockEntity()},
                ModBlocks.BARRELS.values.toTypedArray()).build(null)
        val BLOCK_ENTITY_ID = id("barrel")
    }
}