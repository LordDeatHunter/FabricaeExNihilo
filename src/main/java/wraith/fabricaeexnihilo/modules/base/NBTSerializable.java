package wraith.fabricaeexnihilo.modules.base;

import net.minecraft.nbt.NbtCompound;

interface NBTSerializable {
    NbtCompound writeNbt();
    void readNbt(NbtCompound nbt);
}