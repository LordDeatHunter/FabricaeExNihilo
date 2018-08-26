package exnihilocreatio.items.ore

import exnihilocreatio.texturing.Color
import exnihilocreatio.util.ItemInfo

data class Ore(
        val name: String,
        val color: Color,
        val result: ItemInfo? = null,
        val translations: Map<String, String>? = null,
        val oredictName: String? = null
)
