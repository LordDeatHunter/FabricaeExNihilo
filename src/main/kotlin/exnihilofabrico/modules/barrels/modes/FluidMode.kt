package exnihilofabrico.modules.barrels.modes

import alexiil.mc.lib.attributes.fluid.volume.FluidKeys
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import net.minecraft.nbt.CompoundTag

data class FluidMode(val fluid: FluidVolume = FluidKeys.EMPTY.withAmount(0)): BarrelMode {
    override fun tagKey() = "fluid_mode"

    override fun toTag(): CompoundTag {
        val tag = CompoundTag()
        tag.put(tagKey(), fluid.toTag())
        return tag
    }

    companion object {
        fun fromTag(tag: CompoundTag) =
            FluidMode(FluidVolume.fromTag(tag.getCompound("fluid_mode")))
    }
}