package wraith.fabricaeexnihilo.modules.fluids;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.modules.ModFluids;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;
import wraith.fabricaeexnihilo.modules.base.BaseFluidBlock;
import wraith.fabricaeexnihilo.modules.base.FluidSettings;

public class BloodFluid extends AbstractFluid {
    
    private static final FluidSettings FLUID_SETTINGS = new FluidSettings("blood", false);
    public static final TagKey<Fluid> TAG = TagKey.of(Registry.FLUID_KEY, new Identifier("c:blood"));
    public static final BloodFluid STILL = new BloodFluid(true);
    public static final BucketItem BUCKET = new BucketItem(STILL, ModFluids.BUCKET_ITEM_SETTINGS);
    public static final BloodFluid FLOWING = new BloodFluid(false);
    public static final BaseFluidBlock BLOCK = new BaseFluidBlock(STILL, ModFluids.BLOCK_SETTINGS);
    
    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == STILL || fluid == FLOWING;
    }
    
    public BloodFluid(boolean isStill) {
        super(isStill, FLUID_SETTINGS,
                () -> BLOCK,
                () -> BUCKET,
                () -> FLOWING,
                () -> STILL
        );
    }
    
}