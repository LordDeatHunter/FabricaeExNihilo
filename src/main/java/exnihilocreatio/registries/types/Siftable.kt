package exnihilocreatio.registries.types

import exnihilocreatio.util.ItemInfo

// TODO: MOVE TO API PACKAGE ON BREAKING VERSION CHANGE
data class Siftable(
        val drop: ItemInfo? = null,
        val chance: Float = 0.0f,
        val meshLevel: Int = 0
)
