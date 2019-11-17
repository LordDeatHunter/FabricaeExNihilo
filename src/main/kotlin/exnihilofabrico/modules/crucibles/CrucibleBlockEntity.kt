package exnihilofabrico.modules.crucibles

import alexiil.mc.lib.attributes.Simulation
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
import exnihilofabrico.util.isSimulating
import exnihilofabrico.util.ofSize
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.FluidBlock
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Hand
import net.minecraft.util.Tickable
import net.minecraft.util.hit.BlockHitResult

class CrucibleBlockEntity : BaseBlockEntity(TYPE), Tickable, BlockEntityClientSerializable, ItemInsertable {

    // What stack should be rendered for the queued fluid
    var render: ItemStack = ItemStack.EMPTY
    // Fluid to be made
    var queued: FluidVolume = FluidKeys.EMPTY.withAmount(0)
    // Fluid already made
    var contents: FluidVolume = FluidKeys.EMPTY.withAmount(0)
    var heat: Int = 0

    private var timer: Int = world?.random?.nextInt(ExNihiloFabrico.config.modules.crucibles.tickRate) ?: 0

    /**
     * Item Handling
     */
    override fun attemptInsertion(stack: ItemStack, simulation: Simulation): ItemStack {
        ExNihiloFabrico.LOGGER.info("Attempting to insert $stack")
        val result = (if(isStone()) CRUCIBLE_STONE else CRUCIBLE_WOOD).getResult(stack.item) ?: return stack
        ExNihiloFabrico.LOGGER.info("Result: $result")
        ExNihiloFabrico.LOGGER.info("Queued: $queued")
        ExNihiloFabrico.LOGGER.info("Contents: $contents")
        ExNihiloFabrico.LOGGER.info("Heat: $heat")

        if((contents.canMerge(result) || contents.isEmpty) && (queued.canMerge(result) || queued.isEmpty) && contents.amount + queued.amount + result.amount <= getMaxCapacity()) {
            if(simulation.isAction) {
                queued = FluidVolume.create(result.fluidKey, queued.amount + result.amount)
                render =
                    when {
                        stack.item is BlockItem -> stack.copy()
                        isStone() -> Blocks.COBBLESTONE.asStack()
                        else -> Blocks.OAK_LEAVES.asStack()
                    }
                markDirtyClient()
            }
            return stack.ofSize(stack.count - 1)
        }
        return stack
    }

    override fun tick() {
        if(queued.isEmpty() || contents.amount >= getMaxCapacity() || heat <= 0)
            return
        timer += 1
        if(timer > ExNihiloFabrico.config.modules.crucibles.tickRate) {
            if(!queued.isEmpty()) {
                val amt = Math.min(queued.amount, getProcessingSpeed())
                contents = FluidVolume.create(queued.fluidKey, amt + contents.amount)
                queued.split(amt)
                if(queued.amount <= 0)
                    queued = FluidKeys.EMPTY.withAmount(0)
                markDirtyClient()
            }
            timer = 0
        }
    }

    fun isStone(): Boolean {
        return world?.getBlockState(pos)?.material == Material.STONE
    }

    fun getProcessingSpeed(): Int {
        return if(isStone())
            heat * ExNihiloFabrico.config.modules.crucibles.baseProcessRate
        else
            ExNihiloFabrico.config.modules.crucibles.woodenProcessingRate
    }

    fun getMaxCapacity(): Int {
        return FluidVolume.BUCKET *
                (if(isStone())
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

        // Removing a bucket's worth of fluid
        if(held.item == Items.BUCKET && contents.amount > FluidVolume.BUCKET ) {
            val bucket = contents.rawFluid?.bucketItem?.asStack() ?: return false
            if(!player.isCreative) {
                held.decrement(1)
            }
            player.giveItemStack(bucket)
            contents.split(FluidVolume.BUCKET)
            markDirtyClient()
            return true
        }
        // Adding a meltable item
        val result = attemptInsertion(held, Simulation.ACTION)
        if(result.count != held.count && player.isSimulating() == Simulation.ACTION) {
            held.decrement(1)
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

    fun toTagWithoutWorldInfo(tag: CompoundTag): CompoundTag {
        tag.put("render", render.toTag(CompoundTag()))
        tag.put("contents", contents.toTag())
        tag.put("queued", queued.toTag())
        tag.putInt("heat", heat)
        return tag
    }

    fun fromTagWithoutWorldInfo(tag: CompoundTag) {
        render = ItemStack.fromTag(tag.getCompound("render"))
        contents = FluidVolume.fromTag(tag.getCompound("contents"))
        queued = FluidVolume.fromTag(tag.getCompound("queued"))
        heat = tag.getInt("heat")
    }

    companion object {
        @JvmStatic
        val TYPE: BlockEntityType<CrucibleBlockEntity> =
            BlockEntityType.Builder.create({ CrucibleBlockEntity() }, ModBlocks.CRUCIBLES.values.toTypedArray()).build(null)
        val BLOCK_ENTITY_ID = id("crucible")
    }
}