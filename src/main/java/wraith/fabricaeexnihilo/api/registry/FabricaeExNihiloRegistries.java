package wraith.fabricaeexnihilo.api.registry;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;

import java.io.File;

public final class FabricaeExNihiloRegistries {

    private FabricaeExNihiloRegistries() {}

    public static OreRegistry ORES = new wraith.fabricaeexnihilo.registry.sieve.OreRegistry();
    public static MeshRegistry MESH = new wraith.fabricaeexnihilo.registry.sieve.MeshRegistry();
    public static SieveRegistry SIEVE = new wraith.fabricaeexnihilo.registry.sieve.SieveRegistry();
    public static WitchWaterWorldRegistry WITCHWATER_WORLD = new wraith.fabricaeexnihilo.registry.witchwater.WitchWaterWorldRegistry();
    public static WitchWaterEntityRegistry WITCHWATER_ENTITY = new wraith.fabricaeexnihilo.registry.witchwater.WitchWaterEntityRegistry();
    public static ToolRegistry HAMMER = new wraith.fabricaeexnihilo.registry.ToolRegistry();
    public static ToolRegistry CROOK = new wraith.fabricaeexnihilo.registry.ToolRegistry();
    public static CrucibleHeatRegistry CRUCIBLE_HEAT = new wraith.fabricaeexnihilo.registry.crucible.CrucibleHeatRegistry();
    public static CrucibleRegistry CRUCIBLE_STONE = new wraith.fabricaeexnihilo.registry.crucible.CrucibleRegistry();
    public static CrucibleRegistry CRUCIBLE_WOOD = new wraith.fabricaeexnihilo.registry.crucible.CrucibleRegistry();
    public static AlchemyRegistry BARREL_ALCHEMY = new wraith.fabricaeexnihilo.registry.barrel.AlchemyRegistry();
    public static CompostRegistry BARREL_COMPOST = new wraith.fabricaeexnihilo.registry.barrel.CompostRegistry();
    public static MilkingRegistry BARREL_MILKING = new wraith.fabricaeexnihilo.registry.barrel.MilkingRegistry();
    public static LeakingRegistry BARREL_LEAKING = new wraith.fabricaeexnihilo.registry.barrel.LeakingRegistry();
    public static FluidTransformRegistry BARREL_TRANSFORM = new wraith.fabricaeexnihilo.registry.barrel.FluidTransformRegistry();
    public static FluidOnTopRegistry BARREL_ON_TOP = new wraith.fabricaeexnihilo.registry.barrel.FluidOnTopRegistry();

    private static final File CONFIG_DIR = new File(FabricLoader.getInstance().getConfigDir().toFile(), "fabricaeexnihilo");

    static {
        if(!CONFIG_DIR.exists()) {
            CONFIG_DIR.mkdir();
        }
    }

    public static void loadRecipeRegistries() {
        loadToolRegistries();
        if(FabricaeExNihilo.CONFIG.modules.crucibles.enabled) {
            loadCrucibleRegistries();
        }
        if(FabricaeExNihilo.CONFIG.modules.sieves.enabled) {
            loadSieveRegistry();
        }
        if(FabricaeExNihilo.CONFIG.modules.witchwater.enabled) {
            loadWitchWaterRegistries();
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enabled) {
            loadBarrelRegistries();
        }
    }

    private static void loadBarrelRegistries() {
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableAlchemy) {
            BARREL_ALCHEMY = wraith.fabricaeexnihilo.registry.barrel.AlchemyRegistry.fromJson(new File(CONFIG_DIR,"barrel_alchemy.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableCompost) {
            BARREL_COMPOST = wraith.fabricaeexnihilo.registry.barrel.CompostRegistry.fromJson(new File(CONFIG_DIR,"barrel_compost.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableLeaking) {
            BARREL_MILKING = wraith.fabricaeexnihilo.registry.barrel.MilkingRegistry.fromJson(new File(CONFIG_DIR,"barrel_milking.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableMilking) {
            BARREL_LEAKING = wraith.fabricaeexnihilo.registry.barrel.LeakingRegistry.fromJson(new File(CONFIG_DIR,"barrel_leaking.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableTransforming) {
            BARREL_TRANSFORM = wraith.fabricaeexnihilo.registry.barrel.FluidTransformRegistry.fromJson(new File(CONFIG_DIR,"barrel_transforming.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableFluidOnTop) {
            BARREL_ON_TOP = wraith.fabricaeexnihilo.registry.barrel.FluidOnTopRegistry.fromJson(new File(CONFIG_DIR,"barrel_on_top.json"));
        }
    }

    public static void loadEarlyRegistries() {
        loadOreRegistry();
        if(FabricaeExNihilo.CONFIG.modules.sieves.enabled) {
            loadMeshRegistry();
        }
    }

    private static void loadOreRegistry() {
        ORES = wraith.fabricaeexnihilo.registry.sieve.OreRegistry.fromJson(new File(CONFIG_DIR,"ore.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Ore Registry.");
    }

    private static void loadMeshRegistry() {
        MESH = wraith.fabricaeexnihilo.registry.sieve.MeshRegistry.fromJson(new File(CONFIG_DIR,"sieve_mesh.json"));
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
        HAMMER = wraith.fabricaeexnihilo.registry.ToolRegistry.fromJson(new File(CONFIG_DIR,"tool_hammer.json"), MetaModule.INSTANCE::registerHammer);
        FabricaeExNihilo.LOGGER.info("Loaded Hammer Registry.");
    }

    private static void loadCrookRegistry() {
        CROOK = wraith.fabricaeexnihilo.registry.ToolRegistry.fromJson(new File(CONFIG_DIR,"tool_crook.json"), MetaModule.INSTANCE::registerCrook);
        FabricaeExNihilo.LOGGER.info("Loaded Crook Registry.");
    }

    private static void loadCrucibleRegistries() {
        loadCrucibleHeatRegistry();
        loadCrucibleStoneRegistry();
        loadCrucibleWoodRegistry();
    }

    private static void loadCrucibleHeatRegistry() {
        CRUCIBLE_HEAT = wraith.fabricaeexnihilo.registry.crucible.CrucibleHeatRegistry.fromJson(new File(CONFIG_DIR,"crucible_heat.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Heat Registry.");
    }

    private static void loadCrucibleStoneRegistry() {
        CRUCIBLE_STONE = wraith.fabricaeexnihilo.registry.crucible.CrucibleRegistry.fromJson(new File(CONFIG_DIR,"crucible_stone.json"), MetaModule.INSTANCE::registerCrucibleStone);
        FabricaeExNihilo.LOGGER.info("Loaded Stone Crucible Registry.");
    }

    private static void loadCrucibleWoodRegistry() {
        CRUCIBLE_WOOD = wraith.fabricaeexnihilo.registry.crucible.CrucibleRegistry.fromJson(new File(CONFIG_DIR,"crucible_wood.json"), MetaModule.INSTANCE::registerCrucibleWood);
        FabricaeExNihilo.LOGGER.info("Loaded Wood Crucible Registry.");
    }

    private static void loadSieveRegistry() {
        SIEVE = wraith.fabricaeexnihilo.registry.sieve.SieveRegistry.fromJson(new File(CONFIG_DIR,"sieve_drops.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Sieve Registry.");
    }

    private static void loadWitchWaterRegistries() {
        WITCHWATER_WORLD = wraith.fabricaeexnihilo.registry.witchwater.WitchWaterWorldRegistry.fromJson(new File(CONFIG_DIR,"witchwater_world.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Witch Water World Registry.");
        WITCHWATER_ENTITY = wraith.fabricaeexnihilo.registry.witchwater.WitchWaterEntityRegistry.fromJson(new File(CONFIG_DIR,"witchwater_entity.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Witch Water Entity Registry.");
    }

}
