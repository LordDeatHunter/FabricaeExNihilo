package wraith.fabricaeexnihilo.util;

import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;

public final class FluidUtils {

    private FluidUtils() {}

    public static FluidState getDefaultFluidState(FluidBlock fluidBlock) {
        return fluidBlock.getFluidState(fluidBlock.getDefaultState());
    }

    public static Fluid getFluid(FluidBlock fluidBlock) {
        return getDefaultFluidState(fluidBlock).getFluid();
    }
}
