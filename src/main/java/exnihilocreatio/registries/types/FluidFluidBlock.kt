package exnihilocreatio.registries.types

import exnihilocreatio.util.BlockInfo

// TODO: MOVE TO API PACKAGE ON BREAKING VERSION CHANGE
data class FluidFluidBlock(
        val fluidInBarrel: String? = null,
        val fluidOnTop: String? = null,
        val result: BlockInfo? = null
)