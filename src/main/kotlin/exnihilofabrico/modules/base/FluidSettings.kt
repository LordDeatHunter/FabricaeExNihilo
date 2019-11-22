package exnihilofabrico.modules.base

import exnihilofabrico.id
import net.minecraft.util.Identifier

data class FluidSettings(
    val basePath: String,
    val flowingTexture: Identifier = id("block/fluid/${basePath}_flow"),
    val stillTexture: Identifier = id("block/fluid/${basePath}_still"),
    val isInfinite: Boolean = false
)