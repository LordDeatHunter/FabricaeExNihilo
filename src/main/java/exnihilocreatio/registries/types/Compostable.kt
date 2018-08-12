package exnihilocreatio.registries.types

import exnihilocreatio.texturing.Color
import exnihilocreatio.util.BlockInfo

data class Compostable(
        val value: Float = 0.toFloat(),
        val color: Color? = null,
        val compostBlock: BlockInfo) {

    companion object {
        val EMPTY = Compostable(0f, Color(0), BlockInfo.EMPTY)
    }
}
