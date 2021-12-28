package wraith.fabricaeexnihilo.api.compatibility;

import wraith.fabricaeexnihilo.api.registry.*;

public interface FabricaeExNihiloModule {
    // Sieve Registries
    void registerMesh(MeshRecipeRegistry registry);
    void registerSieve(SieveRecipeRegistry registry);
    
    // Ore Registry
    void registerOres(OreRecipeRegistry registry);
}
