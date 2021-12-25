package wraith.fabricaeexnihilo.api.registry;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.registry.ToolRecipeRegistryImpl;
import wraith.fabricaeexnihilo.registry.sieve.MeshRecipeRegistryImpl;
import wraith.fabricaeexnihilo.registry.sieve.OreRecipeRegistryImpl;
import wraith.fabricaeexnihilo.registry.sieve.SieveRecipeRegistryImpl;
import wraith.fabricaeexnihilo.registry.witchwater.WitchWaterEntityRecipeRegistryImpl;
import wraith.fabricaeexnihilo.registry.witchwater.WitchWaterWorldRecipeRegistryImpl;

import java.io.File;

public final class FabricaeExNihiloRegistries {

    private FabricaeExNihiloRegistries() {}

    public static OreRecipeRegistry ORES = new OreRecipeRegistryImpl();
    public static MeshRecipeRegistry MESH = new MeshRecipeRegistryImpl();
    
    public static SieveRecipeRegistry SIEVE = new SieveRecipeRegistryImpl();
    public static WitchWaterWorldRecipeRegistry WITCHWATER_WORLD = new WitchWaterWorldRecipeRegistryImpl();
    public static WitchWaterEntityRecipeRegistry WITCHWATER_ENTITY = new WitchWaterEntityRecipeRegistryImpl();
    public static ToolRecipeRegistry HAMMER = new ToolRecipeRegistryImpl();
    public static ToolRecipeRegistry CROOK = new ToolRecipeRegistryImpl();
    
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
        if(FabricaeExNihilo.CONFIG.modules.witchwater.enabled) {
            loadWitchWaterRegistries();
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
        FabricaeExNihilo.LOGGER.info("Loaded Ore Registry.");
    }

    private static void loadMeshRegistry() {
        MESH = MeshRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"sieve_mesh.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Mesh Registry.");
    }

    private static void loadToolRegistries() {
        if(FabricaeExNihilo.CONFIG.modules.hammers.enabled) {
            loadHammerRegistry();
        }
        if(FabricaeExNihilo.CONFIG.modules.crooks.enabled) {
            loadCrookRegistry();
        }
        // TODO Wrench Item
    }

    private static void loadHammerRegistry() {
        HAMMER = ToolRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"tool_hammer.json"), MetaModule.INSTANCE::registerHammer);
        FabricaeExNihilo.LOGGER.info("Loaded Hammer Registry.");
    }

    private static void loadCrookRegistry() {
        CROOK = ToolRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"tool_crook.json"), MetaModule.INSTANCE::registerCrook);
        FabricaeExNihilo.LOGGER.info("Loaded Crook Registry.");
    }

    private static void loadSieveRegistry() {
        SIEVE = SieveRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"sieve_drops.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Sieve Registry.");
    }

    private static void loadWitchWaterRegistries() {
        WITCHWATER_WORLD = WitchWaterWorldRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"witchwater_world.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Witch Water World Registry.");
        WITCHWATER_ENTITY = WitchWaterEntityRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"witchwater_entity.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Witch Water Entity Registry.");
    }

}
