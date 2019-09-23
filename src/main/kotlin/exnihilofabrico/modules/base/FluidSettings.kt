package exnihilofabrico.modules.base

import net.minecraft.util.Identifier

data class FluidSettings(
    val flowingTexture: Identifier,
    val stillTexture: Identifier,
    val isInfinite: Boolean
)