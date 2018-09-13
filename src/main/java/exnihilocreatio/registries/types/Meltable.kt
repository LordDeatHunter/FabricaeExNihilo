package exnihilocreatio.registries.types

import exnihilocreatio.util.BlockInfo

data class Meltable @JvmOverloads constructor(
        val fluid: String,
        var amount: Int,
        private var textureOverride: BlockInfo = BlockInfo.EMPTY
) {
    fun copy(): Meltable {
        return Meltable(fluid, amount, textureOverride)
    }

    fun setTextureOverride(textureOverride: BlockInfo): Meltable {
        this.textureOverride = textureOverride
        return this
    }

    fun getTextureOverride(): BlockInfo = textureOverride

    companion object {
        val EMPTY = Meltable("", 0)
    }
}
