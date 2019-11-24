package exnihilofabrico.api.compatibility

import exnihilofabrico.api.registry.*

interface IExNihiloFabricoModule {

    // Barrel Registries
    fun registerAlchemy(registry: IAlchemyRegistry)
    fun registerCompost(registry: ICompostRegistry)
    fun registerLeaking(registry: ILeakingRegistry)
    fun registerFluidOnTop(registry: IFluidOnTopRegistry)
    fun registerFluidTransform(registry: IFluidTransformRegistry)

    fun registerMilking(registry: IMilkingRegistry)

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
    fun registerWitchWaterWorld(registry: IWitchWaterWorldRegistry)
    fun registerWitchWaterEntity(registry: IWitchWaterEntityRegistry)

    // Ore Registry
    fun registerOres(registry: IOreRegistry)
}