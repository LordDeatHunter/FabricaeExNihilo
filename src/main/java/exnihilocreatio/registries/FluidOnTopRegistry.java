package exnihilocreatio.registries;

import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.ItemInfo;
import net.minecraftforge.fluids.Fluid;

@Deprecated
public class FluidOnTopRegistry {

    public void register(Fluid fluidInBarrel, Fluid fluidOnTop, ItemInfo result) {
        ExNihiloRegistryManager.FLUID_ON_TOP_REGISTRY.register(fluidInBarrel, fluidOnTop, result);
    }

    public static boolean isValidRecipe(Fluid fluidInBarrel, Fluid fluidOnTop) {
        return ExNihiloRegistryManager.FLUID_ON_TOP_REGISTRY.isValidRecipe(fluidInBarrel, fluidOnTop);
    }

    public static ItemInfo getTransformedBlock(Fluid fluidInBarrel, Fluid fluidOnTop) {
        return ExNihiloRegistryManager.FLUID_ON_TOP_REGISTRY.getTransformedBlock(fluidInBarrel, fluidOnTop);
    }
}
