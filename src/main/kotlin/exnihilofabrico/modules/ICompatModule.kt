package exnihilofabrico.modules

import exnihilofabrico.api.registry.*

interface ICompatModule {
    // Tool Registries
    fun registerCrook(registry: IToolRegistry)
    fun registerHammer(registry: IToolRegistry)

    // Witch Water Registries
    fun registerWitchWaterEntity(registry: IWitchWaterEntityRegistry)
    fun registerWitchWaterFluid(registry: IWitchWaterFluidRegistry)


    fun registerSieve(registry: ISieveRegistry)

    // Barrel Registries
    fun registerBarrelAlchemy(registry: IBarrelAlchemyRegistry)
    fun registerBarrelMilking(registry: IBarrelMilkingRegistry)

    // Crucible Registries
}