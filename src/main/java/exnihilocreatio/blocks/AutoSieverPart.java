package exnihilocreatio.blocks;

import net.minecraft.util.IStringSerializable;

public enum AutoSieverPart implements IStringSerializable{
    EMPTY(0, "empty"),
    BOX(1, "box"),
    GEAR(2, "gear"),
    ROD(3, "rod");

    private int ID;
    private String name;

    AutoSieverPart(int ID, String name) {
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
