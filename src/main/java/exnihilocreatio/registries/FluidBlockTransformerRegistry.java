package exnihilocreatio.registries;

import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
/**
 * @deprecated use classes from [{@link exnihilocreatio.api.ExNihiloCreatioAPI}]
 */
@Deprecated
public class FluidBlockTransformerRegistry {

    public static void register(Fluid fluid, ItemInfo inputBlock, ItemInfo outputBlock) {
        ExNihiloRegistryManager.FLUID_BLOCK_TRANSFORMER_REGISTRY.register(fluid, inputBlock, outputBlock);
    }

    public static boolean canBlockBeTransformedWithThisFluid(Fluid fluid, ItemStack stack) {
        return ExNihiloRegistryManager.FLUID_BLOCK_TRANSFORMER_REGISTRY.canBlockBeTransformedWithThisFluid(fluid, stack);
    }

    public static ItemInfo getBlockForTransformation(Fluid fluid, ItemStack stack) {
        return new ItemInfo(ExNihiloRegistryManager.FLUID_BLOCK_TRANSFORMER_REGISTRY.getBlockForTransformation(fluid, stack).getItemStack());
    }
}
