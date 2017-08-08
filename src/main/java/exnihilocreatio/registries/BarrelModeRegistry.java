package exnihilocreatio.registries;

import exnihilocreatio.barrel.IBarrelMode;
import exnihilocreatio.barrel.modes.block.BarrelModeBlock;
import exnihilocreatio.barrel.modes.compost.BarrelModeCompost;
import exnihilocreatio.barrel.modes.fluid.BarrelModeFluid;
import exnihilocreatio.barrel.modes.mobspawn.BarrelModeMobSpawn;
import exnihilocreatio.barrel.modes.transform.BarrelModeFluidTransform;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

public class BarrelModeRegistry {

    private static EnumMap<TriggerType, ArrayList<IBarrelMode>> barrelModes = new EnumMap<TriggerType, ArrayList<IBarrelMode>>(TriggerType.class);
    private static HashMap<String, IBarrelMode> barrelModeNames = new HashMap<String, IBarrelMode>();

    public static void registerBarrelMode(IBarrelMode mode, TriggerType type) {
        ArrayList<IBarrelMode> list = barrelModes.get(type);
        if (list == null)
            list = new ArrayList<IBarrelMode>();

        list.add(mode);
        barrelModes.put(type, list);

        barrelModeNames.put(mode.getName(), mode);
    }

    public static ArrayList<IBarrelMode> getModes(TriggerType type) {
        return barrelModes.get(type);
    }

    public static void registerDefaults() {
        registerBarrelMode(new BarrelModeCompost(), TriggerType.ITEM);
        registerBarrelMode(new BarrelModeFluid(), TriggerType.FLUID);
        registerBarrelMode(new BarrelModeBlock(), TriggerType.NONE);
        registerBarrelMode(new BarrelModeFluidTransform(), TriggerType.NONE);
        registerBarrelMode(new BarrelModeMobSpawn(), TriggerType.NONE);
    }

    public static IBarrelMode getModeByName(String name) {
        return barrelModeNames.get(name);
    }

    public enum TriggerType {
        ITEM, FLUID, TICK, NONE
    }

}
