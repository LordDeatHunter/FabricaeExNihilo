package wraith.fabricaeexnihilo.modules.fluids;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.ModFluids;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;
import wraith.fabricaeexnihilo.modules.base.BaseFluidBlock;
import wraith.fabricaeexnihilo.modules.base.FluidSettings;

public class BrineFluid extends AbstractFluid {
    
    private static final FluidSettings FLUID_SETTINGS = new FluidSettings("brine", false);
    public static final Tag.Identified<Fluid> TAG = TagFactory.FLUID.create(new Identifier("c:brine"));
    public static final BrineFluid STILL = new BrineFluid(true);
    public static final BucketItem BUCKET = new BucketItem(STILL, ModFluids.BUCKET_ITEM_SETTINGS);
    public static final BrineFluid FLOWING = new BrineFluid(false);
    public static final BaseFluidBlock BLOCK = new BaseFluidBlock(STILL, ModFluids.BLOCK_SETTINGS);
    
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
