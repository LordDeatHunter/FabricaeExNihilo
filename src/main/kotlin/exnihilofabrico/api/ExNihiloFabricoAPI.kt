package exnihilofabrico.api

import exnihilofabrico.api.compatibility.IExNihiloFabricoModule
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.util.BlockGenerator

object ExNihiloFabricoAPI {
    val BLOCK_GENERATOR: IBlockGenerator = BlockGenerator

    fun registerCompatabilityModule(module: IExNihiloFabricoModule) = MetaModule.modules.add(module)
}