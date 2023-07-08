package wraith.fabricaeexnihilo.modules.fluids;

import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import wraith.fabricaeexnihilo.modules.ModFluids;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;
import wraith.fabricaeexnihilo.modules.base.FluidSettings;

public class BrineFluid extends AbstractFluid {
    private static final FluidSettings FLUID_SETTINGS = new FluidSettings("brine", 0xACC4B9, false, true);
    public static final BrineFluid STILL = new BrineFluid(true);
    public static final BucketItem BUCKET = new BucketItem(STILL, ModFluids.getBucketItemSettings());
    public static final BrineFluid FLOWING = new BrineFluid(false);
    public static final FluidBlock BLOCK = new FluidBlock(STILL, ModFluids.getBlockSettings());

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == STILL || fluid == FLOWING;
    }

    public BrineFluid(boolean isStill) {
        super(isStill, FLUID_SETTINGS,
                () -> BLOCK,
                () -> BUCKET,
                () -> FLOWING,
                () -> STILL
        );
    }

}
