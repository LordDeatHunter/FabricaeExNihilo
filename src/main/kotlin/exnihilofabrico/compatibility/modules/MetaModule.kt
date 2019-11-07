package exnihilofabrico.compatibility.modules

import exnihilofabrico.api.compatibility.IExNihiloFabricoModule
import exnihilofabrico.api.registry.*

object MetaModule: IExNihiloFabricoModule {
    val modules: MutableList<IExNihiloFabricoModule> = mutableListOf()

    init {
        modules.add(ExNihiloFabrico)
        // Check if an mod with a specific module is installed, then add its module to the list
    }

    override fun registerBarrelAlchemy(registry: IBarrelAlchemyRegistry) = modules.forEach { it.registerBarrelAlchemy(registry) }
    override fun registerBarrelMilking(registry: IBarrelMilkingRegistry) = modules.forEach { it.registerBarrelMilking(registry) }
    override fun registerCrucibleHeat(registry: ICrucibleHeatRegistry) = modules.forEach { it.registerCrucibleHeat(registry) }
    override fun registerCrucibleStone(registry: ICrucibleRegistry) = modules.forEach { it.registerCrucibleStone(registry) }
    override fun registerCrucibleWood(registry: ICrucibleRegistry) = modules.forEach { it.registerCrucibleWood(registry) }
    override fun registerOres(registry: IOreRegistry) = modules.forEach { it.registerOres(registry) }
    override fun registerSieve(registry: ISieveRegistry) = modules.forEach { it.registerSieve(registry) }
    override fun registerMesh(registry: IMeshRegistry) = modules.forEach { it.registerMesh(registry) }
    override fun registerCrook(registry: IToolRegistry) = modules.forEach { it.registerCrook(registry) }
    override fun registerHammer(registry: IToolRegistry) = modules.forEach { it.registerHammer(registry) }
    override fun registerWitchWaterFluid(registry: IWitchWaterFluidRegistry) = modules.forEach { it.registerWitchWaterFluid(registry) }
}