package wraith.fabricaeexnihilo.api.registry;

import net.fabricmc.loader.api.FabricLoader;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.registry.ToolRecipeRegistryImpl;
import wraith.fabricaeexnihilo.registry.barrel.*;
import wraith.fabricaeexnihilo.registry.crucible.CrucibleHeatRecipeRegistryImpl;
import wraith.fabricaeexnihilo.registry.crucible.CrucibleRecipeRegistryImpl;
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
    public static CrucibleHeatRecipeRegistry CRUCIBLE_HEAT = new CrucibleHeatRecipeRegistryImpl();
    public static CrucibleRecipeRegistry CRUCIBLE_STONE = new CrucibleRecipeRegistryImpl();
    public static CrucibleRecipeRegistry CRUCIBLE_WOOD = new CrucibleRecipeRegistryImpl();
    public static AlchemyRecipeRegistry BARREL_ALCHEMY = new AlchemyRecipeRegistryImpl();
    public static CompostRecipeRegistry BARREL_COMPOST = new CompostRecipeRegistryImpl();
    public static MilkingRecipeRegistry BARREL_MILKING = new MilkingRecipeRegistryImpl();
    public static LeakingRecipeRegistry BARREL_LEAKING = new LeakingRecipeRegistryImpl();
    public static FluidTransformRecipeRegistry BARREL_TRANSFORM = new FluidTransformRecipeRegistryImpl();
    public static FluidOnTopRecipeRegistry BARREL_ON_TOP = new FluidOnTopRecipeRegistryImpl();

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
            BARREL_ALCHEMY = AlchemyRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"barrel_alchemy.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableCompost) {
            BARREL_COMPOST = CompostRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"barrel_compost.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableLeaking) {
            BARREL_MILKING = MilkingRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"barrel_milking.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableMilking) {
            BARREL_LEAKING = LeakingRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"barrel_leaking.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableTransforming) {
            BARREL_TRANSFORM = FluidTransformRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"barrel_transforming.json"));
        }
        if(FabricaeExNihilo.CONFIG.modules.barrels.enableFluidOnTop) {
            BARREL_ON_TOP = FluidOnTopRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"barrel_on_top.json"));
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

    private static void loadCrucibleRegistries() {
        loadCrucibleHeatRegistry();
        loadCrucibleStoneRegistry();
        loadCrucibleWoodRegistry();
    }

    private static void loadCrucibleHeatRegistry() {
        CRUCIBLE_HEAT = CrucibleHeatRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"crucible_heat.json"));
        FabricaeExNihilo.LOGGER.info("Loaded Heat Registry.");
    }

    private static void loadCrucibleStoneRegistry() {
        CRUCIBLE_STONE = CrucibleRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"crucible_stone.json"), MetaModule.INSTANCE::registerCrucibleStone);
        FabricaeExNihilo.LOGGER.info("Loaded Stone Crucible Registry.");
    }

    private static void loadCrucibleWoodRegistry() {
        CRUCIBLE_WOOD = CrucibleRecipeRegistryImpl.fromJson(new File(CONFIG_DIR,"crucible_wood.json"), MetaModule.INSTANCE::registerCrucibleWood);
        FabricaeExNihilo.LOGGER.info("Loaded Wood Crucible Registry.");
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
