package exnihilofabrico.api.registry

import exnihilofabrico.registry.*

object Registry {
    val WITCHWATER_ENTITY: IWitchWaterEntityRegistry = WitchWaterEntityRegistry()
    val WITCHWATER_WORLD: IWitchWaterFluidRegistry = WitchWaterFluidRegistry()

    val HAMMER: IToolRegistry = ToolRegistry()
    val CROOK: IToolRegistry = ToolRegistry()

    val SIEVE: ISieveRegistry = SieveRegistry()

    val CRUCIBLE_HEAT: ICrucibleHeatRegistry = CrucibleHeatRegistry()
}