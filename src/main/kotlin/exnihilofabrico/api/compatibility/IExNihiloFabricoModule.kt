package exnihilofabrico.api.compatibility

import exnihilofabrico.api.registry.*

interface IExNihiloFabricoModule {

    // Barrel Registries
    fun registerBarrelAlchemy(registry: IBarrelAlchemyRegistry)
    fun registerBarrelMilking(registry: IBarrelMilkingRegistry)

    // Crucible Registries
    fun registerCrucibleHeat(registry: ICrucibleHeatRegistry)
    fun registerCrucibleStone(registry: ICrucibleRegistry)
    fun registerCrucibleWood(registry: ICrucibleRegistry)

    // Sieve Registries
    fun registerMesh(registry: IMeshRegistry)
    fun registerSieve(registry: ISieveRegistry)

    // Tool Registries
    fun registerCrook(registry: IToolRegistry)
    fun registerHammer(registry: IToolRegistry)

    // Witch Water Registries
    fun registerWitchWaterFluid(registry: IWitchWaterFluidRegistry)

    // Ore Registry
    fun registerOres(registry: IOreRegistry)
}