package exnihilocreatio.api.registries

import exnihilocreatio.registries.types.FluidTransformer
import exnihilocreatio.util.BlockInfo

interface IFluidTransformRegistry : IRegistryMappedList<String, FluidTransformer> {
    fun register(inputFluid: String, outputFluid: String, duration: Int, transformingBlocks: Array<BlockInfo>, blocksToSpawn: Array<BlockInfo>);
    fun register(transformer: FluidTransformer)

    fun containsKey(inputFluid: String): Boolean

    fun getFluidTransformer(inputFluid: String, outputFluid: String): FluidTransformer?

    fun getFluidTransformers(inputFluid: String): List<FluidTransformer>
}
