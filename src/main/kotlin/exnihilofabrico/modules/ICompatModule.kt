package exnihilofabrico.modules

import exnihilofabrico.api.registry.*

interface ICompatModule {

    // Barrel Registries
    fun registerBarrelAlchemy(registry: IBarrelAlchemyRegistry)
    fun registerBarrelMilking(registry: IBarrelMilkingRegistry)

    // Crucible Registries
    fun registerCrucibleHeat(registry: ICrucibleHeatRegistry)
    fun registerCrucibleStone(registry: ICrucibleRegistry)
    fun registerCrucibleWood(registry: ICrucibleRegistry)

    // Sieve Registries
    fun registerSieve(registry: ISieveRegistry)

    // Tool Registries
    fun registerCrook(registry: IToolRegistry)
    fun registerHammer(registry: IToolRegistry)

    // Witch Water Registries
    fun registerWitchWaterEntity(registry: IWitchWaterEntityRegistry)
    fun registerWitchWaterFluid(registry: IWitchWaterFluidRegistry)
}