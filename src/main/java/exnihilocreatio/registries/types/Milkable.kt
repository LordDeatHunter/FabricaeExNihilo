package exnihilocreatio.registries.types

// TODO: MOVE TO API PACKAGE ON BREAKING VERSION CHANGE
data class Milkable(
        val entityOnTop: String,
        val result: String,
        val amount: Int,
        val coolDown: Int
)
