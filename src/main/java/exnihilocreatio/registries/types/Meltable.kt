package exnihilocreatio.registries.types

import exnihilocreatio.util.BlockInfo

data class Meltable @JvmOverloads constructor(
        val fluid: String,
        var amount: Int,
        var textureOverride: BlockInfo = BlockInfo.EMPTY
) {
    fun copy(): Meltable {
        return Meltable(fluid, amount, textureOverride)
    }

    fun setTextureOverrideChain(textureOverride: BlockInfo): Meltable {
        this.textureOverride = textureOverride
        return this
    }

    companion object {
        val EMPTY = Meltable("", 0)
    }
}
