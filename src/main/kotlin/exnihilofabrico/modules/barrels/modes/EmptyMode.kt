package exnihilofabrico.modules.barrels.modes

import net.minecraft.nbt.CompoundTag

class EmptyMode: BarrelMode {
    override fun tagKey() = "empty_mode"

    override fun toTag() = CompoundTag()
}