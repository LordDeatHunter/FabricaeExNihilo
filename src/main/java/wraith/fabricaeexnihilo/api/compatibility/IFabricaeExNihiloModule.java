package wraith.fabricaeexnihilo.api.compatibility;

import wraith.fabricaeexnihilo.api.registry.*;

public interface IFabricaeExNihiloModule {

    // Barrel Registries
    void registerAlchemy(IAlchemyRegistry registry);
    void registerCompost(ICompostRegistry registry);
    void registerLeaking(ILeakingRegistry registry);
    void registerFluidOnTop(IFluidOnTopRegistry registry);
    void registerFluidTransform(IFluidTransformRegistry registry);

    void registerMilking(IMilkingRegistry registry);

    // Crucible Registries
    void registerCrucibleHeat(ICrucibleHeatRegistry registry);
    void registerCrucibleStone(ICrucibleRegistry registry);
    void registerCrucibleWood(ICrucibleRegistry registry);

    // Sieve Registries
    void registerMesh(IMeshRegistry registry);
    void registerSieve(ISieveRegistry registry);

    // Tool Registries
    void registerCrook(IToolRegistry registry);
    void registerHammer(IToolRegistry registry);

    // Witch Water Registries
    void registerWitchWaterWorld(IWitchWaterWorldRegistry registry);
    void registerWitchWaterEntity(IWitchWaterEntityRegistry registry);

    // Ore Registry
    void registerOres(IOreRegistry registry);

}
