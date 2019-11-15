package exnihilofabrico.modules.barrels.modes

import exnihilofabrico.api.crafting.FluidStack
import net.minecraft.nbt.CompoundTag

data class FluidMode(val fluid: FluidStack = FluidStack.EMPTY): BarrelMode {
    override fun tagKey() = "fluid_mode"

    override fun toTag(): CompoundTag {
        val tag = CompoundTag()
        tag.put(tagKey(), fluid.toTag())
        return tag
    }

    companion object {
        fun fromTag(tag: CompoundTag) =
            FluidMode(FluidStack.create(tag.getCompound("fluid_mode")))
    }
}