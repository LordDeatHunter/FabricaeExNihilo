package wraith.fabricaeexnihilo.api.registry;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.registry.ToolRegistry;
import wraith.fabricaeexnihilo.registry.barrel.*;
import wraith.fabricaeexnihilo.registry.crucible.CrucibleHeatRegistry;
import wraith.fabricaeexnihilo.registry.crucible.CrucibleRegistry;
import wraith.fabricaeexnihilo.registry.sieve.MeshRegistry;
import wraith.fabricaeexnihilo.registry.sieve.OreRegistry;
import wraith.fabricaeexnihilo.registry.sieve.SieveRegistry;
import wraith.fabricaeexnihilo.registry.witchwater.WitchWaterEntityRegistry;
import wraith.fabricaeexnihilo.registry.witchwater.WitchWaterWorldRegistry;

import java.io.File;

public final class FabricaeExNihiloRegistries {

    private FabricaeExNihiloRegistries() {}

    public static IOreRegistry ORES = new OreRegistry();
    public static IMeshRegistry MESH = new MeshRegistry();
    public static ISieveRegistry SIEVE = new SieveRegistry();
    public static IWitchWaterWorldRegistry WITCHWATER_WORLD = new WitchWaterWorldRegistry();
    public static IWitchWaterEntityRegistry WITCHWATER_ENTITY = new WitchWaterEntityRegistry();
    public static IToolRegistry HAMMER = new ToolRegistry();
    public static IToolRegistry CROOK = new ToolRegistry();
    public static ICrucibleHeatRegistry CRUCIBLE_HEAT = new CrucibleHeatRegistry();
    public static ICrucibleRegistry CRUCIBLE_STONE = new CrucibleRegistry();
    public static ICrucibleRegistry CRUCIBLE_WOOD = new CrucibleRegistry();
    public static IAlchemyRegistry BARREL_ALCHEMY = new AlchemyRegistry();
    public static ICompostRegistry BARREL_COMPOST = new CompostRegistry();
    public static IMilkingRegistry BARREL_MILKING = new MilkingRegistry();
    public static ILeakingRegistry BARREL_LEAKING = new LeakingRegistry();
    public static IFluidTransformRegistry BARREL_TRANSFORM = new FluidTransformRegistry();
    public static IFluidOnTopRegistry BARREL_ON_TOP = new FluidOnTopRegistry();

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
            BARREL_ALCHEMY = AlchemyRegistry.fromJson(new File(CONFIG_DIR,"barrel_alchemy.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableCompost) {
            BARREL_COMPOST = CompostRegistry.fromJson(new File(CONFIG_DIR,"barrel_compost.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableLeaking) {
            BARREL_MILKING = MilkingRegistry.fromJson(new File(CONFIG_DIR,"barrel_milking.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableMilking) {
            BARREL_LEAKING = LeakingRegistry.fromJson(new File(CONFIG_DIR,"barrel_leaking.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableTransforming) {
            BARREL_TRANSFORM = FluidTransformRegistry.fromJson(new File(CONFIG_DIR,"barrel_transforming.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableFluidOnTop) {
            BARREL_ON_TOP = FluidOnTopRegistry.fromJson(new File(CONFIG_DIR,"barrel_on_top.json"));
        }
    }

    public static void loadEarlyRegistries() {
        loadOreRegistry();
        if(FabricaeExNihilo.CONFIG.modules.sieves.enabled) {
            loadMeshRegistry();
        }
    }

    private static void loadOreRegistry() {
        ORES = OreRegistry.fromJson(new File(CONFIG_DIR,"ore.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Ore Registry.");
    }

    private static void loadMeshRegistry() {
        MESH = MeshRegistry.fromJson(new File(CONFIG_DIR,"sieve_mesh.json"));
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
        HAMMER = ToolRegistry.fromJson(new File(CONFIG_DIR,"tool_hammer.json"), MetaModule.INSTANCE::registerHammer);
        FabricaeExNihilo.LOGGER.info("Loaded Hammer Registry.");
    }

    private static void loadCrookRegistry() {
        CROOK = ToolRegistry.fromJson(new File(CONFIG_DIR,"tool_crook.json"), MetaModule.INSTANCE::registerCrook);
        FabricaeExNihilo.LOGGER.info("Loaded Crook Registry.");
    }

    private static void loadCrucibleRegistries() {
        loadCrucibleHeatRegistry();
        loadCrucibleStoneRegistry();
        loadCrucibleWoodRegistry();
    }

    private static void loadCrucibleHeatRegistry() {
        CRUCIBLE_HEAT = CrucibleHeatRegistry.fromJson(new File(CONFIG_DIR,"crucible_heat.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Heat Registry.");
    }

    private static void loadCrucibleStoneRegistry() {
        CRUCIBLE_STONE = CrucibleRegistry.fromJson(new File(CONFIG_DIR,"crucible_stone.json"), MetaModule.INSTANCE::registerCrucibleStone);
        FabricaeExNihilo.LOGGER.info("Loaded Stone Crucible Registry.");
    }

    private static void loadCrucibleWoodRegistry() {
        CRUCIBLE_WOOD = CrucibleRegistry.fromJson(new File(CONFIG_DIR,"crucible_wood.json"), MetaModule.INSTANCE::registerCrucibleWood);
        FabricaeExNihilo.LOGGER.info("Loaded Wood Crucible Registry.");
    }

    private static void loadSieveRegistry() {
        SIEVE = SieveRegistry.fromJson(new File(CONFIG_DIR,"sieve_drops.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Sieve Registry.");
    }

    private static void loadWitchWaterRegistries() {
        WITCHWATER_WORLD = WitchWaterWorldRegistry.fromJson(new File(CONFIG_DIR,"witchwater_world.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Witch Water World Registry.");
        WITCHWATER_ENTITY = WitchWaterEntityRegistry.fromJson(new File(CONFIG_DIR,"witchwater_entity.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Witch Water Entity Registry.");
    }

}
