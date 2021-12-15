package wraith.fabricaeexnihilo.api.compatibility;

import wraith.fabricaeexnihilo.api.registry.*;

public interface FabricaeExNihiloModule {

    // Barrel Registries
    void registerAlchemy(AlchemyRegistry registry);
    void registerCompost(CompostRegistry registry);
    void registerLeaking(LeakingRegistry registry);
    void registerFluidOnTop(FluidOnTopRegistry registry);
    void registerFluidTransform(FluidTransformRegistry registry);

    void registerMilking(MilkingRegistry registry);

    // Crucible Registries
    void registerCrucibleHeat(CrucibleHeatRegistry registry);
    void registerCrucibleStone(CrucibleRegistry registry);
    void registerCrucibleWood(CrucibleRegistry registry);

    // Sieve Registries
    void registerMesh(MeshRegistry registry);
    void registerSieve(SieveRegistry registry);

    // Tool Registries
    void registerCrook(ToolRegistry registry);
    void registerHammer(ToolRegistry registry);

    // Witch Water Registries
    void registerWitchWaterWorld(WitchWaterWorldRegistry registry);
    void registerWitchWaterEntity(WitchWaterEntityRegistry registry);

    // Ore Registry
    void registerOres(OreRegistry registry);

}
