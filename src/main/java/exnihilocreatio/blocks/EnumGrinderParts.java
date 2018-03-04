package exnihilocreatio.blocks;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public enum EnumGrinderParts implements IStringSerializable {
    EMPTY(0, "empty"),
    BOX(1, "box"),
    GEAR(2, "gear"),
    ROD(3, "rod");

    private final int ID;
    private final String name;

    EnumGrinderParts(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    @Override
    @Nonnull
    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return getName();
    }
}
