package exnihilocreatio.blocks;

import net.minecraft.util.IStringSerializable;

public enum EnumAutoSifterParts implements IStringSerializable {
    EMPTY(0, "empty"),
    BOX(1, "box"),
    PISTON(2, "piston"),
    ROD(3, "rod"),
    CONNECTION(4, "connection");

    private final int ID;
    private final String name;

    EnumAutoSifterParts(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    @Override
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
