package exnihilofabrico.modules.barrels.modes

import exnihilofabrico.util.Color
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag

data class CompostMode(val result: ItemStack = ItemStack.EMPTY, var amount: Double = 0.0, var color: Color = Color.WHITE): BarrelMode {

    var progress = 0.0

    override fun tagKey() = "compost_mode"

    override fun toTag(): CompoundTag {
        val tag = CompoundTag()
        tag.put("result", result.toTag(CompoundTag()))
        tag.putDouble("amount", amount)
        tag.putInt("color", color.toInt())
        tag.putDouble("progress", progress)
        return tag
    }

    companion object {
        fun fromTag(tag: CompoundTag): CompostMode {
            val mode = CompostMode(ItemStack.fromTag(tag.getCompound("result")), tag.getDouble("amount"), Color(tag.getInt("color")))
            mode.progress = tag.getDouble("progress")
            return mode
        }
    }
}