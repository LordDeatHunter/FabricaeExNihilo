package exnihilocreatio.registries;

import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.FluidTransformer;
import exnihilocreatio.util.BlockInfo;

import java.util.ArrayList;
/**
 * @deprecated use classes from [{@link exnihilocreatio.api.ExNihiloCreatioAPI}]
 */
@Deprecated
public class FluidTransformRegistry {

    public static void register(String inputFluid, String outputFluid, int duration, BlockInfo[] transformingBlocks, BlockInfo[] blocksToSpawn) {
        ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY.register(inputFluid, outputFluid, duration, transformingBlocks, blocksToSpawn);
    }

    public static void register(FluidTransformer transformer) {
        ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY.register(transformer);
    }

    public static boolean containsKey(String inputFluid) {
        return ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY.containsKey(inputFluid);
    }

    public static FluidTransformer getFluidTransformer(String inputFluid, String outputFluid) {
        return ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY.getFluidTransformer(inputFluid, outputFluid);
    }

    public static ArrayList<FluidTransformer> getFluidTransformers(String inputFluid) {
        return (ArrayList<FluidTransformer>) ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY.getFluidTransformers(inputFluid);
    }
}
