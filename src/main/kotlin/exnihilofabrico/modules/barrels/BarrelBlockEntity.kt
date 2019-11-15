package exnihilofabrico.modules.barrels

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.id
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.barrels.modes.*
import exnihilofabrico.modules.base.BaseBlockEntity
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Tickable
import net.minecraft.util.math.Direction

class BarrelBlockEntity(var mode: BarrelMode = EmptyMode()): BaseBlockEntity(TYPE), Tickable,
    BlockEntityClientSerializable, SidedInventory {

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
        if(mode is ItemMode) {
            mode = ItemMode(stack.copy())
            return
        }

        (mode as? FluidMode)?.let {
            // TODO Check for Alchemy Recipes
        }

        (mode as? EmptyMode)?.let {
            // TODO Check for Compost Recipes
        }

        (mode as? CompostMode)?.let {
            // TODO Check for Compost Recipes
        }

    }

    override fun removeInvStack(slot: Int): ItemStack {
        if(mode is ItemMode) {
            val stack = (mode as ItemMode).stack
            clear()
            return stack
        }
        return ItemStack.EMPTY
    }

    override fun canPlayerUseInv(player: PlayerEntity?) = false
    override fun getInvSize() = 1

    override fun takeInvStack(slot: Int, amount: Int): ItemStack {
        return (mode as? ItemMode)?.let{
            val stack = it.stack.split(amount)
            if(it.stack.isEmpty)
                mode = EmptyMode()
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
            (mode as? FluidMode)?.let {
                // TODO Check for Alchemy Recipes
                false
            } ?: false ||
            (mode as? EmptyMode)?.let {
                // TODO Check for Compost Recipes
                false
            } ?: false
    }

    override fun tick() {
        (mode as? FluidMode)?.let {
            //TODO check for leaking
            //TODO check for nearby block changes
            //TODO check adjacent fluid changes
        }
        (mode as? AlchemyMode)?.let {
            // TODO make progress
        }
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
        mode = BarrelModeFactory(tag)
    }

    companion object {
        val TYPE: BlockEntityType<BarrelBlockEntity> =
            BlockEntityType.Builder.create({BarrelBlockEntity()},
                ModBlocks.BARRELS.values.toTypedArray()).build(null)
        val BLOCK_ENTITY_ID = id("barrel")
    }
}