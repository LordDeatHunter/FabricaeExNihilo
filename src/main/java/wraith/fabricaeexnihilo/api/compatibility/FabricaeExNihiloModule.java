package wraith.fabricaeexnihilo.api.compatibility;

import wraith.fabricaeexnihilo.api.registry.*;

public interface FabricaeExNihiloModule {
    // Crucible Registries
    void registerCrucibleHeat(CrucibleHeatRecipeRegistry registry);
    void registerCrucibleStone(CrucibleRecipeRegistry registry);
    void registerCrucibleWood(CrucibleRecipeRegistry registry);

    // Sieve Registries
    void registerMesh(MeshRecipeRegistry registry);
    void registerSieve(SieveRecipeRegistry registry);

    // Tool Registries
    void registerCrook(ToolRecipeRegistry registry);
    void registerHammer(ToolRecipeRegistry registry);

    // Witch Water Registries
    void registerWitchWaterWorld(WitchWaterWorldRecipeRegistry registry);
    void registerWitchWaterEntity(WitchWaterEntityRecipeRegistry registry);

    // Ore Registry
    void registerOres(OreRecipeRegistry registry);

}
