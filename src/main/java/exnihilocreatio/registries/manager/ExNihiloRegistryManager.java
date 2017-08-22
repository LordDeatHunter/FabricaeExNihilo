package exnihilocreatio.registries.manager;

import exnihilocreatio.registries.registries.CompostRegistryNew;
import exnihilocreatio.registries.registries.CrookRegistryNew;
import exnihilocreatio.registries.registries.HammerRegistryNew;
import exnihilocreatio.registries.registries.SieveRegistryNew;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExNihiloRegistryManager {

    public static final CompostRegistryNew COMPOST_REGISTRY = new CompostRegistryNew();
    public static final CrookRegistryNew CROOK_REGISTRY = new CrookRegistryNew();
    public static final SieveRegistryNew SIEVE_REGISTRY = new SieveRegistryNew();
    public static final HammerRegistryNew HAMMER_REGISTRY = new HammerRegistryNew();


    //region >>>> DEFAULT RECIPE PROVIDERS
    public static final List<ISieveDefaultRegistryProvider> SIEVE_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IHammerDefaultRegistryProvider> HAMMER_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>(); //TODO: Change back to arraylist
    @Getter
    public static final List<ICompostDefaultRegistryProvider> defaultCompostRecipeHandlers = new ArrayList<>();
    @Getter
    public static final List<ICrookDefaultRegistryProvider> defaultCrookRecipeHandlers = new ArrayList<>();
    @Getter
    public static final List<ICrucibleDefaultRegistryProvider> defaultCrucibleRecipeHandlers = new ArrayList<>();
    @Getter
    public static final List<IFluidBlockDefaultRegistryProvider> defaultFluidBlockRecipeHandlers = new ArrayList<>();
    @Getter
    public static final List<IFluidOnTopDefaultRegistryProvider> defaultFluidOnTopRecipeHandlers = new ArrayList<>();
    @Getter
    public static final List<IFluidTransformDefaultRegistryProvider> defaultFluidTransformRecipeHandlers = new ArrayList<>();
    @Getter
    public static final List<IHeatDefaultRegistryProvider> defaultHeatRecipeHandlers = new ArrayList<>();
    @Getter
    public static final List<IOreDefaultRegistryProvider> defaultOreRecipeHandlers = new ArrayList<>();
    //endregion


    //region >>>> DEFAULT RECIPE REGISTERS

    public static void registerSieveDefaultRecipeHandler(ISieveDefaultRegistryProvider provider) {
        SIEVE_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerHammerDefaultRecipeHandler(IHammerDefaultRegistryProvider provider) {
        HAMMER_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerCompostDefaultRecipeHandler(ICompostDefaultRegistryProvider provider) {
        defaultCompostRecipeHandlers.add(provider);
    }

    public static void registerCrookDefaultRecipeHandler(ICrookDefaultRegistryProvider provider) {
        defaultCrookRecipeHandlers.add(provider);
    }

    public static void registerCrucibleDefaultRecipeHandler(ICrucibleDefaultRegistryProvider provider) {
        defaultCrucibleRecipeHandlers.add(provider);
    }

    public static void registerFluidBlockDefaultRecipeHandler(IFluidBlockDefaultRegistryProvider provider) {
        defaultFluidBlockRecipeHandlers.add(provider);
    }

    public static void registerFluidTransformDefaultRecipeHandler(IFluidTransformDefaultRegistryProvider provider) {
        defaultFluidTransformRecipeHandlers.add(provider);
    }

    public static void registerFluidOnTopDefaultRecipeHandler(IFluidOnTopDefaultRegistryProvider provider) {
        defaultFluidOnTopRecipeHandlers.add(provider);
    }

    public static void registerHeatDefaultRecipeHandler(IHeatDefaultRegistryProvider provider) {
        defaultHeatRecipeHandlers.add(provider);
    }

    public static void registerOreDefaultRecipeHandler(IOreDefaultRegistryProvider provider) {
        defaultOreRecipeHandlers.add(provider);
    }
    //endregion
}
