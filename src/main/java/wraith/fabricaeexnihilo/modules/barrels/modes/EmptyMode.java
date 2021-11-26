package wraith.fabricaeexnihilo.modules.barrels.modes;

import net.minecraft.nbt.NbtCompound;

public class EmptyMode implements BarrelMode {

    @Override
    public NbtCompound writeNbt() {
        return new NbtCompound();
    }

    @Override
    public String nbtKey() {
        return "empty_mode";
    }

}
