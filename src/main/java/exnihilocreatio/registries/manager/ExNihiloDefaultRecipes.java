package exnihilocreatio.registries.manager;

import exnihilocreatio.registries.registries.*;
import org.jetbrains.annotations.NotNull;


public class ExNihiloDefaultRecipes {
    private static final CompatDefaultRecipes compat = new CompatDefaultRecipes();

    public static void registerDefaults() {
        ExNihiloRegistryManager.registerSieveDefaultRecipeHandler(new SieveDefaults());
        ExNihiloRegistryManager.registerHammerDefaultRecipeHandler(new HammerDefaults());
        ExNihiloRegistryManager.registerCompostDefaultRecipeHandler(new CompostDefaults());
        ExNihiloRegistryManager.registerCrookDefaultRecipeHandler(new CrookDefaults());
        ExNihiloRegistryManager.registerHeatDefaultRecipeHandler(new HeatDefaults());
        ExNihiloRegistryManager.registerOreDefaultRecipeHandler(new OreDefaults());
        ExNihiloRegistryManager.registerBarrelLiquidBlacklistDefaultHandler(new BarrelLiquidBlacklistDefaults());
        ExNihiloRegistryManager.registerFluidOnTopDefaultRecipeHandler(new FluidOnTopDefaults());
        ExNihiloRegistryManager.registerFluidTransformDefaultRecipeHandler(new FluidTransformDefaults());
        ExNihiloRegistryManager.registerFluidBlockDefaultRecipeHandler(new FluidBlockTransformDefaults());
        ExNihiloRegistryManager.registerFluidItemFluidDefaultHandler(new FluidItemFluidDefaults());
        ExNihiloRegistryManager.registerCrucibleStoneDefaultRecipeHandler(new CrucibleStoneDefaults());
        ExNihiloRegistryManager.registerCrucibleWoodDefaultRecipeHandler(new CrucibleWoodDefaults());
        ExNihiloRegistryManager.registerMilkEntityDefaultRecipeHandler(new MilkEntityDefaults());
        ExNihiloRegistryManager.registerWitchWaterWorldDefaultRecipeHandler(new WitchWaterWorldDefaults());
    }

    private static class CompostDefaults implements ICompostDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(@NotNull CompostRegistry registry) {
            compat.registerCompost(registry);
        }
    }

    private static class CrookDefaults implements ICrookDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(@NotNull CrookRegistry registry) {
            compat.registerCrook(registry);
        }
    }

    private static class SieveDefaults implements ISieveDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(@NotNull SieveRegistry registry) {
            compat.registerSieve(registry);
        }
    }

    private static class HammerDefaults implements IHammerDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(@NotNull HammerRegistry registry) {
            compat.registerHammer(registry);
        }
    }

    private static class HeatDefaults implements IHeatDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(@NotNull HeatRegistry registry) {
            compat.registerHeat(registry);
        }
    }

    private static class BarrelLiquidBlacklistDefaults implements IBarrelLiquidBlacklistDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(@NotNull BarrelLiquidBlacklistRegistry registry) {
            compat.registerBarrel(registry);
        }
    }

    private static class FluidOnTopDefaults implements IFluidOnTopDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(@NotNull FluidOnTopRegistry registry) {
            compat.registerFluidOnTop(registry);
        }
    }

    private static class OreDefaults implements IOreDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(@NotNull OreRegistry registry) {
            compat.registerOreChunks(registry);
        }
    }

    private static class FluidTransformDefaults implements IFluidTransformDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(@NotNull FluidTransformRegistry registry) {
            compat.registerFluidTransform(registry);
        }
    }

    private static class FluidBlockTransformDefaults implements IFluidBlockDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(@NotNull FluidBlockTransformerRegistry registry) {
            compat.registerFluidBlockTransform(registry);
        }
    }

    private static class FluidItemFluidDefaults implements IFluidItemFluidDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(@NotNull FluidItemFluidRegistry registry) {
            compat.registerFluidItemFluid(registry);
        }
    }

    private static class CrucibleStoneDefaults implements ICrucibleStoneDefaultRegistryProvider {

        @Override
        public void registerRecipeDefaults(@NotNull CrucibleRegistry registry) {
            compat.registerCrucibleStone(registry);
        }
    }

    private static class CrucibleWoodDefaults implements ICrucibleWoodDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(@NotNull CrucibleRegistry registry) {
            compat.registerCrucibleWood(registry);
        }
    }

    public static class MilkEntityDefaults implements IMilkEntityDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(@NotNull MilkEntityRegistry registry) {
            compat.registerMilk(registry);
        }
    }

    public static class WitchWaterWorldDefaults implements IWitchWaterWorldDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(@NotNull WitchWaterWorldRegistry registry) {
            compat.registerWitchWaterWorld(registry);
        }
    }
}
