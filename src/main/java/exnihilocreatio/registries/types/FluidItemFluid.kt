package exnihilocreatio.registries.types

import exnihilocreatio.util.StackInfo

// TODO: MOVE TO API PACKAGE ON BREAKING VERSION CHANGE
data class FluidItemFluid(
        val inputFluid: String? = null,
        val reactant: StackInfo? = null,
        val output: String? = null
)
