package exnihilocreatio.registries.manager;

import exnihilocreatio.registries.registries.*;

import java.util.ArrayList;
import java.util.List;

public final class ExNihiloRegistryManager {

    //region >>>> DEFAULT RECIPE PROVIDERS
    public static final List<ISieveDefaultRegistryProvider> SIEVE_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IHammerDefaultRegistryProvider> HAMMER_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<ICompostDefaultRegistryProvider> COMPOST_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<ICrookDefaultRegistryProvider> CROOK_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<ICrucibleStoneDefaultRegistryProvider> CRUCIBLE_STONE_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<ICrucibleWoodDefaultRegistryProvider> CRUCIBLE_WOOD_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IFluidBlockDefaultRegistryProvider> FLUID_BLOCK_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IFluidOnTopDefaultRegistryProvider> FLUID_ON_TOP_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IFluidTransformDefaultRegistryProvider> FLUID_TRANSFORM_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IHeatDefaultRegistryProvider> HEAT_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IOreDefaultRegistryProvider> ORE_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IBarrelLiquidBlacklistDefaultRegistryProvider> BARREL_LIQUID_BLACKLIST_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    //endregion

    public static final CompostRegistryNew COMPOST_REGISTRY = new CompostRegistryNew();
    public static final CrookRegistryNew CROOK_REGISTRY = new CrookRegistryNew();
    public static final SieveRegistryNew SIEVE_REGISTRY = new SieveRegistryNew();
    public static final HammerRegistryNew HAMMER_REGISTRY = new HammerRegistryNew();
    public static final HeatRegistryNew HEAT_REGISTRY = new HeatRegistryNew();
    public static final OreRegistryNew ORE_REGISTRY = new OreRegistryNew();
    public static final BarrelLiquidBlacklistRegistryNew BARREL_LIQUID_BLACKLIST_REGISTRY = new BarrelLiquidBlacklistRegistryNew();
    public static final FluidOnTopRegistryNew FLUID_ON_TOP_REGISTRY = new FluidOnTopRegistryNew();
    public static final FluidTransformRegistryNew FLUID_TRANSFORM_REGISTRY = new FluidTransformRegistryNew();
    public static final FluidBlockTransformerRegistryNew FLUID_BLOCK_TRANSFORMER_REGISTRY = new FluidBlockTransformerRegistryNew();
    public static final CrucibleRegistryNew CRUCIBLE_STONE_REGISTRY = new CrucibleRegistryNew(CRUCIBLE_STONE_DEFAULT_REGISTRY_PROVIDERS);
    public static final CrucibleRegistryNew CRUCIBLE_WOOD_REGISTRY = new CrucibleRegistryNew(CRUCIBLE_WOOD_DEFAULT_REGISTRY_PROVIDERS);


    //region >>>> DEFAULT RECIPE REGISTERS

    public static void registerSieveDefaultRecipeHandler(ISieveDefaultRegistryProvider provider) {
        SIEVE_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerHammerDefaultRecipeHandler(IHammerDefaultRegistryProvider provider) {
        HAMMER_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerCompostDefaultRecipeHandler(ICompostDefaultRegistryProvider provider) {
        COMPOST_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerCrookDefaultRecipeHandler(ICrookDefaultRegistryProvider provider) {
        CROOK_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerCrucibleStoneDefaultRecipeHandler(ICrucibleStoneDefaultRegistryProvider provider) {
        CRUCIBLE_STONE_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerCrucibleWoodDefaultRecipeHandler(ICrucibleWoodDefaultRegistryProvider provider) {
        CRUCIBLE_WOOD_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerFluidBlockDefaultRecipeHandler(IFluidBlockDefaultRegistryProvider provider) {
        FLUID_BLOCK_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerFluidTransformDefaultRecipeHandler(IFluidTransformDefaultRegistryProvider provider) {
        FLUID_TRANSFORM_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerFluidOnTopDefaultRecipeHandler(IFluidOnTopDefaultRegistryProvider provider) {
        FLUID_ON_TOP_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerHeatDefaultRecipeHandler(IHeatDefaultRegistryProvider provider) {
        HEAT_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerOreDefaultRecipeHandler(IOreDefaultRegistryProvider provider) {
        ORE_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerBarrelLiquidBlacklistDefaultHandler(IBarrelLiquidBlacklistDefaultRegistryProvider provider) {
        BARREL_LIQUID_BLACKLIST_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }
    //endregion
}
