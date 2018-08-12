package exnihilocreatio.registries.types

import exnihilocreatio.util.BlockInfo
import lombok.EqualsAndHashCode
import lombok.Getter

@EqualsAndHashCode
data class FluidTransformer(
                       val inputFluid: String,
                       val outputFluid: String,
                       val duration: Int,
                       val transformingBlocks: List<BlockInfo>,
                       val blocksToSpawn: List<BlockInfo>)
