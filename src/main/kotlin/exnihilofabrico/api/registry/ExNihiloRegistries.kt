package exnihilofabrico.api.registry

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.registry.*
import net.fabricmc.loader.FabricLoader
import java.io.File

object ExNihiloRegistries {

    @JvmField
    var WITCHWATER_WORLD: IWitchWaterFluidRegistry = WitchWaterFluidRegistry()

    @JvmField
    var HAMMER: IToolRegistry = ToolRegistry()
    @JvmField
    var CROOK: IToolRegistry = ToolRegistry()

    @JvmField
    var MESH: IMeshRegistry = MeshRegistry()
    @JvmField
    var SIEVE: ISieveRegistry = SieveRegistry()

    @JvmField
    var CRUCIBLE_HEAT: ICrucibleHeatRegistry = CrucibleHeatRegistry()
    @JvmField
    var CRUCIBLE_STONE: ICrucibleRegistry = CrucibleRegistry()
    @JvmField
    var CRUCIBLE_WOOD: ICrucibleRegistry = CrucibleRegistry()

    @JvmField
    var ORES: IOreRegistry = OreRegistry()


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
            loadWitchWaterRegistry()
    }

    fun loadEarlyRegistries() {
        loadOreRegistry()
        if(ExNihiloFabrico.config.modules.sieves.enabled)
            loadMeshRegistry()
    }

    fun loadOreRegistry() {
        ORES = OreRegistry.fromJson(
            File(
                configDir,
                "ore.json"
            )
        )
        ExNihiloFabrico.LOGGER.info("Loaded Ore Registry.")
    }

    fun loadMeshRegistry() {
        MESH = MeshRegistry.fromJson(
            File(
                configDir,
                "sieve_mesh.json"
            )
        )
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
            File(
                configDir,
                "tool_hammer.json"
            ), MetaModule::registerHammer
        )
        ExNihiloFabrico.LOGGER.info("Loaded Hammer Registry.")
    }

    fun loadCrookRegistry() {
        CROOK = ToolRegistry.fromJson(
            File(
                configDir,
                "tool_crook.json"
            ), MetaModule::registerCrook
        )
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
                File(
                    configDir,
                    "crucible_heat.json"
                )
            )
        ExNihiloFabrico.LOGGER.info("Loaded Heat Registry.")
    }

    fun loadCrucibleStoneRegistry() {
        CRUCIBLE_STONE =
            CrucibleRegistry.fromJson(
                File(
                    configDir,
                    "crucible_stone.json"
                ), MetaModule::registerCrucibleStone
            )
        ExNihiloFabrico.LOGGER.info("Loaded Stone Crucible Registry.")
    }

    fun loadCrucibleWoodRegistry() {
        CRUCIBLE_WOOD =
            CrucibleRegistry.fromJson(
                File(
                    configDir,
                    "crucible_wood.json"
                ), MetaModule::registerCrucibleWood
            )
        ExNihiloFabrico.LOGGER.info("Loaded Wood Crucible Registry.")
    }

    fun loadSieveRegistry() {
        SIEVE = SieveRegistry.fromJson(
            File(
                configDir,
                "sieve_drops.json"
            )
        )
        ExNihiloFabrico.LOGGER.info("Loaded Sieve Registry.")
    }

    fun loadWitchWaterRegistry() {
        WITCHWATER_WORLD =
            WitchWaterFluidRegistry.fromJson(
                File(
                    configDir,
                    "witchwater_fluid.json"
                )
            )
        ExNihiloFabrico.LOGGER.info("Loaded Witch Water Fluid Registry.")
    }
}