package wraith.fabricaeexnihilo.util;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class FluidUtils {

    private FluidUtils() {}

    public static FluidState getDefaultFluidState(FluidBlock fluidBlock) {
        return fluidBlock.getFluidState(fluidBlock.getDefaultState());
    }

    public static Fluid getFluid(FluidBlock fluidBlock) {
        return getDefaultFluidState(fluidBlock).getFluid();
    }
}
