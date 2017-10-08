package exnihilocreatio.compatibility.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;

import java.util.ArrayList;
import java.util.List;

public class CrTIntegration {
    public static List<IAction> removeActions = new ArrayList<>();
    public static List<IAction> addActions = new ArrayList<>();

    public static void loadIActions(){
        removeActions.forEach(iAction -> {
            CraftTweakerAPI.logInfo(iAction.describe());
            iAction.apply();
        });

        addActions.forEach(iAction -> {
            CraftTweakerAPI.logInfo(iAction.describe());
            iAction.apply();
        });
    }
}
