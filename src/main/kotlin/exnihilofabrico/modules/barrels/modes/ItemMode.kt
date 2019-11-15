package exnihilofabrico.modules.barrels.modes

import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag

data class ItemMode(var stack: ItemStack = ItemStack.EMPTY): BarrelMode {
    override fun tagKey() = "item_mode"

    override fun toTag(): CompoundTag {
        val tag = CompoundTag()
        tag.put("item_mode", stack.toTag(CompoundTag()))
        return tag
    }

    companion object {
        fun fromTag(tag: CompoundTag) =
            ItemMode(ItemStack.fromTag(tag.getCompound("item_mode")))
    }
}