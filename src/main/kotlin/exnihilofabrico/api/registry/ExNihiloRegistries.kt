package exnihilofabrico.api.registry

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.registry.ToolRegistry
import exnihilofabrico.registry.barrel.*
import exnihilofabrico.registry.crucible.CrucibleHeatRegistry
import exnihilofabrico.registry.crucible.CrucibleRegistry
import exnihilofabrico.registry.sieve.MeshRegistry
import exnihilofabrico.registry.sieve.OreRegistry
import exnihilofabrico.registry.sieve.SieveRegistry
import exnihilofabrico.registry.witchwater.WitchWaterEntityRegistry
import exnihilofabrico.registry.witchwater.WitchWaterWorldRegistry
import net.fabricmc.loader.FabricLoader
import java.io.File

object ExNihiloRegistries {

    @JvmField
    var ORES: IOreRegistry = OreRegistry()
    @JvmField
    var MESH: IMeshRegistry = MeshRegistry()
    @JvmField
    var SIEVE: ISieveRegistry = SieveRegistry()

    @JvmField
    var WITCHWATER_WORLD: IWitchWaterWorldRegistry = WitchWaterWorldRegistry()
    @JvmField
    var WITCHWATER_ENTITY: IWitchWaterEntityRegistry = WitchWaterEntityRegistry()

    @JvmField
    var HAMMER: IToolRegistry = ToolRegistry()
    @JvmField
    var CROOK: IToolRegistry = ToolRegistry()

    @JvmField
    var CRUCIBLE_HEAT: ICrucibleHeatRegistry = CrucibleHeatRegistry()
    @JvmField
    var CRUCIBLE_STONE: ICrucibleRegistry = CrucibleRegistry()
    @JvmField
    var CRUCIBLE_WOOD: ICrucibleRegistry = CrucibleRegistry()

    @JvmField
    var BARREL_ALCHEMY: IAlchemyRegistry = AlchemyRegistry()
    var BARREL_COMPOST: ICompostRegistry = CompostRegistry()
    var BARREL_MILKING: IMilkingRegistry = MilkingRegistry()
    var BARREL_LEAKING: ILeakingRegistry = LeakingRegistry()
    var BARREL_TRANSFORM: IFluidTransformRegistry = FluidTransformRegistry()
    var BARREL_ON_TOP: IFluidOnTopRegistry = FluidOnTopRegistry()

    private val configDir = File(FabricLoader.INSTANCE.configDirectory, "exnihilofabrico")

    init {
        if(!configDir.exists())
            configDir.mkdir()
    }

    fun loadRecipeRegistries() {
        loadToolRegistries()
        if(ExNihiloFabrico.config.modules.crucibles.enabled)
            loadCrucibleRegistries()
        if(ExNihiloFabrico.config.modules.sieves.enabled)
            loadSieveRegistry()
        if(ExNihiloFabrico.config.modules.witchwater.enabled)
            loadWitchWaterRegistries()
        if(ExNihiloFabrico.config.modules.barrels.enabled)
            loadBarrelRegistries()
    }

    private fun loadBarrelRegistries() {
        if(ExNihiloFabrico.config.modules.barrels.enableAlchemy)
            BARREL_ALCHEMY = AlchemyRegistry.fromJson(
                File(configDir,"barrel_alchemy.json"))
        if(ExNihiloFabrico.config.modules.barrels.enableCompost)
            BARREL_COMPOST = CompostRegistry.fromJson(
                File(configDir,"barrel_compost.json"))
        if(ExNihiloFabrico.config.modules.barrels.enableLeaking)
            BARREL_MILKING = MilkingRegistry.fromJson(
                File(configDir,"barrel_milking.json"))
        if(ExNihiloFabrico.config.modules.barrels.enableMilking)
            BARREL_LEAKING = LeakingRegistry.fromJson(
                File(configDir,"barrel_leaking.json"))
        if(ExNihiloFabrico.config.modules.barrels.enableTransforming)
            BARREL_TRANSFORM = FluidTransformRegistry.fromJson(
                File(configDir,"barrel_transforming.json"))
        if(ExNihiloFabrico.config.modules.barrels.enableFluidOnTop)
            BARREL_ON_TOP = FluidOnTopRegistry.fromJson(
                File(configDir,"barrel_on_top.json"))
    }

    fun loadEarlyRegistries() {
        loadOreRegistry()
        if(ExNihiloFabrico.config.modules.sieves.enabled)
            loadMeshRegistry()
    }

    fun loadOreRegistry() {
        ORES = OreRegistry.fromJson(
            File(configDir,"ore.json"))
        ExNihiloFabrico.LOGGER.info("Loaded Ore Registry.")
    }

    fun loadMeshRegistry() {
        MESH = MeshRegistry.fromJson(
            File(configDir,"sieve_mesh.json"))
        ExNihiloFabrico.LOGGER.info("Loaded Mesh Registry.")
    }

    fun loadToolRegistries() {
        if(ExNihiloFabrico.config.modules.hammer.enabled)
            loadHammerRegistry()
        if(ExNihiloFabrico.config.modules.crooks.enabled)
            loadCrookRegistry()
        // TODO Wrench Item
    }

    fun loadHammerRegistry() {
        HAMMER = ToolRegistry.fromJson(
            File(configDir,"tool_hammer.json"),
            MetaModule::registerHammer)
        ExNihiloFabrico.LOGGER.info("Loaded Hammer Registry.")
    }

    fun loadCrookRegistry() {
        CROOK = ToolRegistry.fromJson(
            File(configDir,"tool_crook.json"),
            MetaModule::registerCrook)
        ExNihiloFabrico.LOGGER.info("Loaded Crook Registry.")
    }

    fun loadCrucibleRegistries() {
        loadCrucibleHeatRegistry()
        loadCrucibleStoneRegistry()
        loadCrucibleWoodRegistry()
    }

    fun loadCrucibleHeatRegistry() {
        CRUCIBLE_HEAT =
            CrucibleHeatRegistry.fromJson(
                File(configDir,"crucible_heat.json"))
        ExNihiloFabrico.LOGGER.info("Loaded Heat Registry.")
    }

    fun loadCrucibleStoneRegistry() {
        CRUCIBLE_STONE =
            CrucibleRegistry.fromJson(
                File(configDir,"crucible_stone.json"),
                MetaModule::registerCrucibleStone)
        ExNihiloFabrico.LOGGER.info("Loaded Stone Crucible Registry.")
    }

    fun loadCrucibleWoodRegistry() {
        CRUCIBLE_WOOD =
            CrucibleRegistry.fromJson(
                File(configDir,"crucible_wood.json"),
                MetaModule::registerCrucibleWood)
        ExNihiloFabrico.LOGGER.info("Loaded Wood Crucible Registry.")
    }

    fun loadSieveRegistry() {
        SIEVE = SieveRegistry.fromJson(
            File(configDir,"sieve_drops.json"))
        ExNihiloFabrico.LOGGER.info("Loaded Sieve Registry.")
    }

    fun loadWitchWaterRegistries() {
        WITCHWATER_WORLD =
            WitchWaterWorldRegistry.fromJson(
                File(configDir,"witchwater_world.json"))
        ExNihiloFabrico.LOGGER.info("Loaded Witch Water World Registry.")
        WITCHWATER_ENTITY =
            WitchWaterEntityRegistry.fromJson(
                File(configDir,"witchwater_entity.json"))
        ExNihiloFabrico.LOGGER.info("Loaded Witch Water Entity Registry.")
    }
}