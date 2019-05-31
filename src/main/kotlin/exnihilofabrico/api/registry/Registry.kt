package exnihilofabrico.api.registry

import exnihilofabrico.registry.SieveRegistry
import exnihilofabrico.registry.ToolRegistry
import exnihilofabrico.registry.WitchWaterEntityRegistry
import exnihilofabrico.registry.WitchWaterFluidRegistry

object Registry {
    val WITCHWATER_ENTITY: IWitchWaterEntityRegistry = WitchWaterEntityRegistry()
    val WITCHWATER_WORLD: IWitchWaterFluidRegistry = WitchWaterFluidRegistry()

    val HAMMER: IToolRegistry = ToolRegistry()
    val CROOK: IToolRegistry = ToolRegistry()

    val SIEVE: ISieveRegistry = SieveRegistry()
}