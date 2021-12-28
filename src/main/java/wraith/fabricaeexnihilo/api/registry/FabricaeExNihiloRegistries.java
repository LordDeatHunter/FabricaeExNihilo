package wraith.fabricaeexnihilo.api.registry;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.registry.sieve.MeshRecipeRegistryImpl;
import wraith.fabricaeexnihilo.registry.sieve.OreRecipeRegistryImpl;
import wraith.fabricaeexnihilo.registry.sieve.SieveRecipeRegistryImpl;

import java.io.File;

public final class FabricaeExNihiloRegistries {

    private FabricaeExNihiloRegistries() {}

    public static OreRecipeRegistry ORES = new OreRecipeRegistryImpl();
    public static MeshRecipeRegistry MESH = new MeshRecipeRegistryImpl();
    
    public static SieveRecipeRegistry SIEVE = new SieveRecipeRegistryImpl();
    
    private static final File CONFIG_DIR = new File(FabricLoader.getInstance().getConfigDir().toFile(), "fabricaeexnihilo");

    static {
        if(!CONFIG_DIR.exists()) {
            CONFIG_DIR.mkdir();
        }
    }

    // TODO: Reimplement config for recipes. Could use a custom resource condition once they are added to fapi
    public static void loadRecipeRegistries() {
        loadToolRegistries();
        if(FabricaeExNihilo.CONFIG.modules.sieves.enabled) {
            loadSieveRegistry();
        }
    }

    public static void loadEarlyRegistries() {
        loadOreRegistry();
        if(FabricaeExNihilo.CONFIG.modules.sieves.enabled) {
            loadMeshRegistry();
        }
    }

    private static void loadOreRegistry() {
        ORES = OreRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"ore.json"));
        FabricaeExNihilo.LOGGER.debug("Loaded Ore Registry.");
    }

    private static void loadMeshRegistry() {
        MESH = MeshRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"sieve_mesh.json"));
        FabricaeExNihilo.LOGGER.debug("Loaded Mesh Registry.");
    }

    private static void loadToolRegistries() {
        // TODO Wrench Item
    }

    private static void loadSieveRegistry() {
        SIEVE = SieveRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"sieve_drops.json"));
        FabricaeExNihilo.LOGGER.debug("Loaded Sieve Registry.");
    }
}
