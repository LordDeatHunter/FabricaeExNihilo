package wraith.fabricaeexnihilo.modules.fluids;

import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.world.WorldView;
import wraith.fabricaeexnihilo.modules.ModFluids;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;
import wraith.fabricaeexnihilo.modules.base.FluidSettings;

public class BloodFluid extends AbstractFluid {
    private static final FluidSettings FLUID_SETTINGS = new FluidSettings("blood", 0x6F0000, false, false);
    public static final BloodFluid FLOWING = new BloodFluid(false);
    public static final BloodFluid STILL = new BloodFluid(true);
    public static final BucketItem BUCKET = new BucketItem(STILL, ModFluids.getBucketItemSettings());
    public static final FluidBlock BLOCK = new FluidBlock(STILL, ModFluids.getBlockSettings());

    public BloodFluid(boolean isStill) {
        super(isStill, FLUID_SETTINGS,
                () -> BLOCK,
                () -> BUCKET,
                () -> FLOWING,
                () -> STILL
        );
    }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == STILL || fluid == FLOWING;
    }

    @Override
    public int getFlowSpeed(WorldView world) {
        return 3;
    }

}