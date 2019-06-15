package exnihilofabrico.common.crucibles

import exnihilofabrico.ExNihiloConfig
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_HEAT
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_STONE
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_WOOD
import exnihilofabrico.common.ModBlocks
import exnihilofabrico.id
import io.github.prospector.silk.fluid.DropletValues
import io.github.prospector.silk.fluid.FluidContainer
import io.github.prospector.silk.fluid.FluidInstance
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.FluidBlock
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Hand
import net.minecraft.util.Tickable
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Direction

class CrucibleBlockEntity : BlockEntity(TYPE), FluidContainer, Tickable, BlockEntityClientSerializable {

    var render: ItemStack = ItemStack.EMPTY
    var queued: FluidInstance = FluidInstance.EMPTY
    var contents: FluidInstance = FluidInstance.EMPTY
    var heat: Int = 0

    private var timer: Int = 0
    private val stone: Boolean = world?.getBlockState(pos)?.material == Material.STONE

    override fun markDirty() {
        super.markDirty()

        world?.apply {
            updateListeners(pos, getBlockState(pos), getBlockState(pos), 3)
        }
    }

    override fun tick() {
        if(queued.isEmpty || contents.amount >= maxCapacity || heat <= 0)
            return
        timer += 1
        if(timer > ExNihiloConfig.Modules.Crucibles.tickRate) {
            val amt = Math.min(queued.amount, heat)
            contents.addAmount(amt)
            queued.subtractAmount(amt)
            timer = 0
            markDirty()
        }
    }

    fun activate(state: BlockState?, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?): Boolean {
        if(world?.isClient != false || player == null)
            return true
        val held = player.getStackInHand(hand ?: player.activeHand) ?: ItemStack.EMPTY

        if(held.isEmpty)
            return false

        // Removing a bucket's worth of fluid
        if(held.item == Items.BUCKET) {
            if(contents.amount > DropletValues.BUCKET) {
                val bucket = ItemStack(contents.fluid.bucketItem)
                if(!player.isCreative) {
                    held.subtractAmount(1)
                    contents.subtractAmount(DropletValues.BUCKET)
                }
                player.giveItemStack(bucket)
                markDirty()
            }
            return true
        }
        // Adding a meltable item
        val registry = if(stone) CRUCIBLE_STONE else CRUCIBLE_WOOD
        val result = registry.getResult(held.item)
        if(result != null && // Has result
            (contents.isEmpty || result.fluid == contents.fluid) && // And contents doesn't clash
            (queued.isEmpty || result.fluid == queued.fluid) && // And queued doesn't clash
            queued.amount + contents.amount + result.amount < maxCapacity) {
            render = held.copy()
            if(!player.isCreative)
                held.subtractAmount(1)
            if(queued.isEmpty)
                queued = result.copy()
            else
                queued.addAmount(result.amount)
            markDirty()
            return true
        }
        return true
    }

    fun updateHeat() {
        val oldheat = heat
        val state = world?.getBlockState(pos.down()) ?: return
        val block = state.block
        if(block is FluidBlock) {
            val fluidState = block.getFluidState(state)
            heat = Math.round(CRUCIBLE_HEAT.getHeat(fluidState.fluid) * fluidState.fluid.getHeight(fluidState, world, pos.down())).toInt()
        }
        else {
            heat = CRUCIBLE_HEAT.getHeat(block)
        }
        if(heat != oldheat)
            markDirty()
    }

    /**
     * FluidContainer
     */
    override fun extractFluid(direction: Direction?, fluid: Fluid?, amount: Int) {
        if(contents.fluid == fluid && contents.amount >= amount)
            contents.subtractAmount(amount)
    }

    override fun getMaxCapacity(): Int {
        return 4 * DropletValues.BUCKET
    }

    override fun canExtractFluid(direction: Direction?, fluid: Fluid?, amount: Int): Boolean {
        return contents.fluid == fluid && contents.amount >= amount
    }

    override fun getFluids(direction: Direction?): Array<FluidInstance> {
        return arrayOf(contents)
    }

    override fun setFluid(direction: Direction?, fluid: FluidInstance?) {
        contents = fluid ?: FluidInstance.EMPTY
    }

    override fun canInsertFluid(direction: Direction?, fluid: Fluid?, amount: Int): Boolean {
        return false
    }

    override fun insertFluid(direction: Direction?, fluid: Fluid?, amount: Int) {
        return // Do nothing
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
        tag.put("contents", contents.toTag(CompoundTag()))
        tag.put("queued", queued.toTag(CompoundTag()))
        tag.put("render", render.toTag(CompoundTag()))
        tag.putInt("heat", heat)
        return tag
    }

    fun fromTagWithoutWorldInfo(tag: CompoundTag) {
        contents.fromTag(tag.getCompound("contents"))
        queued.fromTag(tag.getCompound("queued"))
        render = ItemStack.fromTag(tag.getCompound("render"))
        heat = tag.getInt("heat")
    }

    companion object {
        @JvmStatic
        val TYPE: BlockEntityType<CrucibleBlockEntity> =
            BlockEntityType.Builder.create({ CrucibleBlockEntity() }, ModBlocks.CRUCIBLES.values.toTypedArray()).build(null)
        val BLOCK_ENTITY_ID = id("crucible")
    }
}