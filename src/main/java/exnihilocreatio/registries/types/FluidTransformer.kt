package exnihilocreatio.registries.types

import exnihilocreatio.util.BlockInfo
import java.util.*

// TODO: MOVE TO API PACKAGE ON BREAKING VERSION CHANGE
data class FluidTransformer(
        val inputFluid: String,
        val outputFluid: String,
        val duration: Int,
        val transformingBlocks: Array<BlockInfo>,
        val blocksToSpawn: Array<BlockInfo>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FluidTransformer

        if (inputFluid != other.inputFluid) return false
        if (outputFluid != other.outputFluid) return false
        if (duration != other.duration) return false
        if (!Arrays.equals(transformingBlocks, other.transformingBlocks)) return false
        if (!Arrays.equals(blocksToSpawn, other.blocksToSpawn)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = inputFluid.hashCode()
        result = 31 * result + outputFluid.hashCode()
        result = 31 * result + duration
        result = 31 * result + Arrays.hashCode(transformingBlocks)
        result = 31 * result + Arrays.hashCode(blocksToSpawn)
        return result
    }
}
