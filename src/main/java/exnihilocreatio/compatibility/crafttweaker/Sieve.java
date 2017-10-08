package exnihilocreatio.compatibility.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import exnihilocreatio.compatibility.crafttweaker.prefab.ENCBaseRemove;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.exnihilocreatio.Sieve")
@ZenRegister
public class Sieve {
    public static final String NAME = "Sieve";

    @ZenMethod
    public static void removeAll(){
        CrTIntegration.removeActions.add(new RemoveAll());
    }

    private static class RemoveAll extends ENCBaseRemove {
        RemoveAll() {
            super(NAME);
        }

        @Override
        public void apply() {
            System.out.println("pre-size = " + ExNihiloRegistryManager.SIEVE_REGISTRY.getRegistry().size());
            ExNihiloRegistryManager.SIEVE_REGISTRY.clearRegistry();
            System.out.println("post-size = " + ExNihiloRegistryManager.SIEVE_REGISTRY.getRegistry().size());
        }

        @Override
        public String describe() {
            return "Removing all recipes for the Ex Nihilo Sieve.";
        }
    }
}
