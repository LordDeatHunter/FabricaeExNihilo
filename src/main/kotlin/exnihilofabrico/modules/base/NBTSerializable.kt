package exnihilofabrico.modules.base

import net.minecraft.nbt.CompoundTag

interface NBTSerializable {
    fun toTag(): CompoundTag
    fun fromTag(tag: CompoundTag)
}