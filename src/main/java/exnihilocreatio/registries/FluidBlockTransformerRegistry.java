package exnihilocreatio.registries;

import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.IStackInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

@Deprecated
public class FluidBlockTransformerRegistry {

    public static void register(Fluid fluid, IStackInfo inputBlock, IStackInfo outputBlock) {
        ExNihiloRegistryManager.FLUID_BLOCK_TRANSFORMER_REGISTRY.register(fluid, inputBlock, outputBlock);
    }

    public static boolean canBlockBeTransformedWithThisFluid(Fluid fluid, ItemStack stack) {
        return ExNihiloRegistryManager.FLUID_BLOCK_TRANSFORMER_REGISTRY.canBlockBeTransformedWithThisFluid(fluid, stack);
    }

    public static IStackInfo getBlockForTransformation(Fluid fluid, ItemStack stack) {
        return ExNihiloRegistryManager.FLUID_BLOCK_TRANSFORMER_REGISTRY.getBlockForTransformation(fluid, stack);
    }
}
