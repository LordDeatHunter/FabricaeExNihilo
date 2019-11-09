package exnihilofabrico.modules.crucibles

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.crafting.FluidStack
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_HEAT
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_STONE
import exnihilofabrico.api.registry.ExNihiloRegistries.CRUCIBLE_WOOD
import exnihilofabrico.id
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.base.BaseBlockEntity
import exnihilofabrico.util.asStack
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.FluidBlock
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Hand
import net.minecraft.util.Tickable
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.registry.Registry

class CrucibleBlockEntity : BaseBlockEntity(TYPE), Tickable, BlockEntityClientSerializable {

    // What stack should be rendered for the queued fluid
    var render: ItemStack = ItemStack.EMPTY
    // Fluid to be made
    var queued: FluidStack = FluidStack.EMPTY
    // Fluid already made
    var contents: FluidStack = FluidStack.EMPTY
    var heat: Int = 0

    private var timer: Int = world?.random?.nextInt(ExNihiloFabrico.config.modules.crucibles.tickRate) ?: 0

    override fun tick() {
        if(queued.isEmpty() || contents.amount >= getMaxCapacity() || heat <= 0)
            return
        timer += 1
        if(timer > ExNihiloFabrico.config.modules.crucibles.tickRate) {
            if(!queued.isEmpty()) {
                val amt = if(isStone()) Math.min(queued.amount, heat) else Math.min(queued.amount, ExNihiloFabrico.config.modules.crucibles.tickRate)
                contents = FluidStack(queued.fluid, amt + contents.amount)
                queued.amount -= amt
                if(queued.amount <= 0)
                    queued = FluidStack.EMPTY
                markDirtyClient()
            }
            timer = 0
        }
    }

    fun isStone(): Boolean {
        return world?.getBlockState(pos)?.material == Material.STONE
    }

    fun getMaxCapacity(): Int {
        return FluidStack.BUCKET_AMOUNT *
                (if(isStone()) ExNihiloFabrico.config.modules.crucibles.stoneVolume else ExNihiloFabrico.config.modules.crucibles.woodVolume)
    }

    fun activate(state: BlockState?, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?): Boolean {
        if(world?.isClient != false || player == null)
            return true

        val held = player.getStackInHand(hand ?: player.activeHand) ?: ItemStack.EMPTY

        if(held.isEmpty)
            return false

        // Removing a bucket's worth of fluid
        if(held.item == Items.BUCKET && contents.amount > FluidStack.BUCKET_AMOUNT ) {
            val bucket = Registry.FLUID[contents.fluid].bucketItem.asStack()
            if(!player.isCreative) {
                held.decrement(1)
            }
            player.giveItemStack(bucket)
            contents.amount -= FluidStack.BUCKET_AMOUNT
            markDirtyClient()
            return true
        }
        // Adding a meltable item
        val registry = if(isStone()) CRUCIBLE_STONE else CRUCIBLE_WOOD
        val result = registry.getResult(held.item)
        if(result != null && // Has result
            (contents.isEmpty() || result.fluid == contents.fluid) && // And contents doesn't clash
            (queued.isEmpty() || result.fluid == queued.fluid) && // And queued doesn't clash
            queued.amount + contents.amount + result.amount <= getMaxCapacity()) {
            render = held.copy()
            if(!player.isCreative)
                held.decrement(1)
            if(queued.isEmpty())
                queued = result.copy()
            else
                queued.amount += result.amount
            markDirtyClient()
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
            heat = Math.round(CRUCIBLE_HEAT.getHeat(fluidState.fluid) * fluidState.fluid.getHeight(fluidState, world, pos.down()))
        }
        else {
            heat = CRUCIBLE_HEAT.getHeat(block)
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
        contents = FluidStack.create(tag.getCompound("contents"))
        queued = FluidStack.create(tag.getCompound("queued"))
        heat = tag.getInt("heat")
    }

    companion object {
        @JvmStatic
        val TYPE: BlockEntityType<CrucibleBlockEntity> =
            BlockEntityType.Builder.create({ CrucibleBlockEntity() }, ModBlocks.CRUCIBLES.values.toTypedArray()).build(null)
        val BLOCK_ENTITY_ID = id("crucible")
    }
}