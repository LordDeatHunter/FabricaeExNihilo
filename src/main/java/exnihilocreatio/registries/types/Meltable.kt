package exnihilocreatio.registries.types

import exnihilocreatio.util.BlockInfo
import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode

@AllArgsConstructor
@EqualsAndHashCode(exclude = arrayOf("textureOverride"))
data class Meltable @JvmOverloads constructor(
        val fluid: String,
        var amount: Int,
        var textureOverride: BlockInfo = BlockInfo.EMPTY
) {
    fun copy(): Meltable {
        return Meltable(fluid, amount, textureOverride)
    }

    fun setTextureOverride(textureOverride: BlockInfo): Meltable {
        this.textureOverride = textureOverride
        return this
    }

    companion object {
        val EMPTY = Meltable("", 0)
    }
}
