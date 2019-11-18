package exnihilofabrico.modules.crucibles

import alexiil.mc.lib.attributes.Simulation
import alexiil.mc.lib.attributes.fluid.FluidExtractable
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter
import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import alexiil.mc.lib.attributes.item.ItemInsertable
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_HEAT
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_STONE
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_WOOD
import exnihilofabrico.id
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.base.BaseBlockEntity
import exnihilofabrico.util.asStack
import exnihilofabrico.util.ofSize
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.FluidBlock
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Hand
import net.minecraft.util.Tickable
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Direction

class CrucibleBlockEntity(private val isStone: Boolean = false):
    BaseBlockEntity(TYPE), Tickable, BlockEntityClientSerializable {

    // What stack should be rendered for the queued fluid
    var render: ItemStack = ItemStack.EMPTY
    // Fluid to be made
    var queued: FluidVolume = FluidKeys.EMPTY.withAmount(0)
    // Fluid already made
    var contents: FluidVolume = FluidKeys.EMPTY.withAmount(0)
    var heat: Int = 0

    private var timer: Int = world?.random?.nextInt(ExNihiloFabrico.config.modules.crucibles.tickRate) ?: ExNihiloFabrico.config.modules.crucibles.tickRate

    val itemInserter = ItemInserter(this)
    val fluidExtractor = FluidExtractor(this)
    val inventory = CrucibleInventory(this)

    override fun tick() {
        if(queued.isEmpty() || contents.amount >= getMaxCapacity() || (heat <= 0 && isStone))
            return
        timer -= 1
        if(timer <= 0) {
            if(!queued.isEmpty()) {
                val amt = Math.min(queued.amount, getProcessingSpeed())
                contents = FluidVolume.create(queued.fluidKey, amt + contents.amount)
                queued.split(amt)
                if(queued.amount <= 0)
                    queued = FluidKeys.EMPTY.withAmount(0)
                markDirtyClient()
            }
            timer = ExNihiloFabrico.config.modules.crucibles.tickRate
        }
    }

    fun getProcessingSpeed(): Int {
        return if(isStone)
            heat * ExNihiloFabrico.config.modules.crucibles.baseProcessRate
        else
            ExNihiloFabrico.config.modules.crucibles.woodenProcessingRate
    }

    fun getMaxCapacity(): Int {
        return FluidVolume.BUCKET *
                (if(isStone)
                    ExNihiloFabrico.config.modules.crucibles.stoneVolume
                else
                    ExNihiloFabrico.config.modules.crucibles.woodVolume)

    }

    fun activate(state: BlockState?, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?): Boolean {
        if(world?.isClient != false || player == null)
            return true

        val held = player.getStackInHand(hand ?: player.activeHand) ?: ItemStack.EMPTY

        if(held.isEmpty)
            return false

        if(contents.amount >= FluidVolume.BUCKET)
            (held.item as? IBucketItem)?.let { bucket ->
                val drainStack = contents.split(bucket.libblockattributes__getFluidVolumeAmount())
                val returnStack = bucket.libblockattributes__withFluid(drainStack.fluidKey)
                if(!player.isCreative) {
                    held.decrement(1)
                }
                player.giveItemStack(returnStack)
                markDirtyClient()
                return true
            }

/*        // Removing a bucket's worth of fluid
        if(held.item == Items.BUCKET && contents.amount > FluidVolume.BUCKET ) {
            val bucket = contents.rawFluid?.bucketItem?.asStack() ?: return true
            if(!player.isCreative) {
                held.decrement(1)
            }
            player.giveItemStack(bucket)
            contents.split(FluidVolume.BUCKET)
            markDirtyClient()
            return true
        }*/
        // Adding a meltable item
        val result = itemInserter.attemptInsertion(held, Simulation.ACTION)
        if(result.count != held.count) {
            if(!player.isCreative)
                held.decrement(1)
            return true
        }
        return true
    }

    fun updateHeat() {
        val oldheat = heat
        val state = world?.getBlockState(pos.down()) ?: return
        val block = state.block
        heat = if(block is FluidBlock) {
            val fluidState = block.getFluidState(state)
            Math.round(CRUCIBLE_HEAT.getHeat(fluidState.fluid) * fluidState.fluid.getHeight(fluidState, world, pos.down()))
        } else {
            CRUCIBLE_HEAT.getHeat(block)
        }
        if(heat != oldheat)
            markDirty()
    }

    /**
     * NBT Serialization section
     */

    override fun toTag(tag: CompoundTag) = toTagWithoutWorldInfo(super.toTag(tag))

    override fun fromTag(tag: CompoundTag?) {
        super.fromTag(tag)
        if(tag==null){
            ExNihiloFabrico.LOGGER.warn("A crucible at $pos is missing data.")
            return
        }
        fromTagWithoutWorldInfo(tag)
    }

    override fun toClientTag(tag: CompoundTag?) = toTag(tag ?: CompoundTag())
    override fun fromClientTag(tag: CompoundTag?) = fromTag(tag ?: CompoundTag())

    private fun toTagWithoutWorldInfo(tag: CompoundTag): CompoundTag {
        tag.put("render", render.toTag(CompoundTag()))
        tag.put("contents", contents.toTag())
        tag.put("queued", queued.toTag())
        tag.putInt("heat", heat)
        return tag
    }

    private fun fromTagWithoutWorldInfo(tag: CompoundTag) {
        render = ItemStack.fromTag(tag.getCompound("render"))
        contents = FluidVolume.fromTag(tag.getCompound("contents"))
        queued = FluidVolume.fromTag(tag.getCompound("queued"))
        heat = tag.getInt("heat")
    }

    /**
     * FluidHandling
     */
    class FluidExtractor(val crucible: CrucibleBlockEntity): FluidExtractable {
        override fun attemptExtraction(filter: FluidFilter?, amount: Int, simulate: Simulation): FluidVolume {
            return if(!crucible.contents.isEmpty && amount > 0 && filter?.matches(crucible.contents.fluidKey) == true) {
                val toDrain = minOf(amount, crucible.contents.amount)
                if(simulate.isAction) {
                    val toReturn = crucible.contents.split(toDrain)
                    crucible.markDirtyClient()
                    toReturn
                }
                else {
                    FluidVolume.create(crucible.contents.fluidKey, toDrain)
                }
            }
            else {
                FluidKeys.EMPTY.withAmount(0)
            }
        }

    }

    /**
     * Item Handling
     */
    class ItemInserter(val crucible: CrucibleBlockEntity): ItemInsertable{
        override fun attemptInsertion(stack: ItemStack, simulation: Simulation): ItemStack {
            val result = (if(crucible.isStone) CRUCIBLE_STONE else CRUCIBLE_WOOD).getResult(stack.item) ?: return stack

            if((crucible.contents.canMerge(result) || crucible.contents.isEmpty)
                && (crucible.queued.canMerge(result) || crucible.queued.isEmpty)
                && crucible.contents.amount + crucible.queued.amount + result.amount <= crucible.getMaxCapacity()) {
                if(simulation.isAction) {
                    crucible.queued = FluidVolume.create(result.fluidKey, crucible.queued.amount + result.amount)
                    crucible.render =
                        when {
                            stack.item is BlockItem -> stack.copy()
                            crucible.isStone -> Blocks.COBBLESTONE.asStack()
                            else -> Blocks.OAK_LEAVES.asStack()
                        }
                    crucible.markDirtyClient()
                }
                return stack.ofSize(stack.count - 1)
            }
            return stack
        }
    }

    class CrucibleInventory(val crucible: CrucibleBlockEntity): SidedInventory {
        override fun getInvStack(slot: Int) = ItemStack.EMPTY
        override fun markDirty() = crucible.markDirtyClient()
        override fun clear() {}
        override fun setInvStack(slot: Int, stack: ItemStack) {
            if(!stack.isEmpty)
                crucible.itemInserter.attemptInsertion(stack, Simulation.ACTION)
        }
        override fun removeInvStack(slot: Int) = ItemStack.EMPTY
        override fun canPlayerUseInv(player: PlayerEntity) = false
        override fun getInvAvailableSlots(direction: Direction?) = IntArray(1) {0}
        override fun getInvSize() = 1
        override fun canExtractInvStack(slot: Int, stack: ItemStack, direction: Direction?) = false
        override fun takeInvStack(slot: Int, amount: Int) = ItemStack.EMPTY
        override fun isInvEmpty() = crucible.contents.amount + crucible.queued.amount < crucible.getMaxCapacity()
        override fun canInsertInvStack(slot: Int, stack: ItemStack?, p2: Direction?): Boolean {
            if(stack == null)
                return false
            return (crucible.itemInserter.attemptInsertion(stack, Simulation.SIMULATE).count != stack.count)
        }

    }

    companion object {
        @JvmStatic
        val TYPE: BlockEntityType<CrucibleBlockEntity> =
            BlockEntityType.Builder.create({ CrucibleBlockEntity() }, ModBlocks.CRUCIBLES.values.toTypedArray()).build(null)
        val BLOCK_ENTITY_ID = id("crucible")
    }
}