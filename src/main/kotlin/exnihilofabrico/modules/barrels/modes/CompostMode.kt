package exnihilofabrico.modules.barrels.modes

import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag

data class CompostMode(val result: ItemStack = ItemStack.EMPTY, var amount: Double = 0.0): BarrelMode {

    override fun tagKey() = "compost_mode"

    override fun toTag(): CompoundTag {
        val tag = CompoundTag()
        tag.put("result", result.toTag(CompoundTag()))
        tag.putDouble("amount", amount)
        return tag
    }

    companion object {
        fun fromTag(tag: CompoundTag) =
            CompostMode(ItemStack.fromTag(tag.getCompound("result")), tag.getDouble("amount"))
    }
}