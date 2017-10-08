package exnihilocreatio.compatibility.crafttweaker;

import crafttweaker.IAction;

import java.util.ArrayList;
import java.util.List;

public class CrTIntegration {
    public static List<IAction> removeActions = new ArrayList<>();
    public static List<IAction> addActions = new ArrayList<>();

    public static void loadIActions(){
        removeActions.forEach(IAction::apply);
        addActions.forEach(IAction::apply);
    }
}
