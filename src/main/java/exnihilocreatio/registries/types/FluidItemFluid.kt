package exnihilocreatio.registries.types

import exnihilocreatio.util.StackInfo

data class FluidItemFluid(
        val inputFluid: String? = null,
        val reactant: StackInfo? = null,
        val output: String? = null
)
