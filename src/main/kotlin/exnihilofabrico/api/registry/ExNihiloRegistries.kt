package exnihilofabrico.api.registry

import exnihilofabrico.api.recipes.CrucibleRecipe
import exnihilofabrico.registry.*
import net.minecraft.util.registry.Registry

object ExNihiloRegistries {
    val WITCHWATER_ENTITY: IWitchWaterEntityRegistry = WitchWaterEntityRegistry()
    val WITCHWATER_WORLD: IWitchWaterFluidRegistry = WitchWaterFluidRegistry()

    val HAMMER: IToolRegistry = ToolRegistry()
    val CROOK: IToolRegistry = ToolRegistry()

    val SIEVE: ISieveRegistry = SieveRegistry()

    val CRUCIBLE_HEAT: ICrucibleHeatRegistry = CrucibleHeatRegistry()
    val CRUCIBLE_STONE: ICrucibleRegistry = CrucibleRegistry()
    val CRUCIBLE_WOOD: ICrucibleRegistry = CrucibleRegistry()
}