package exnihilocreatio.compatibility.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;

import java.util.ArrayList;
import java.util.List;

public class CrTIntegration {
    public static final List<IAction> removeActions = new ArrayList<>();
    public static final List<IAction> addActions = new ArrayList<>();

    public static void loadIActions() {
        removeActions.forEach(CraftTweakerAPI::apply);
        addActions.forEach(CraftTweakerAPI::apply);
    }
}
