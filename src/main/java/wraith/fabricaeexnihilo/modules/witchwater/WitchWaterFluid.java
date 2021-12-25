package wraith.fabricaeexnihilo.modules.witchwater;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.tag.Tag;
import wraith.fabricaeexnihilo.modules.ModFluids;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;
import wraith.fabricaeexnihilo.modules.base.FluidSettings;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class WitchWaterFluid extends AbstractFluid  {
        // TODO: make tag
    private static final FluidSettings FLUID_SETTINGS = new FluidSettings("witchwater", false);
    public static final WitchWaterFluid STILL = new WitchWaterFluid(true);
    public static final Tag.Identified<Fluid> TAG = TagFactory.FLUID.create(id("witchwater"));
    public static final BucketItem BUCKET = new BucketItem(STILL, ModFluids.BUCKET_ITEM_SETTINGS);
    public static final WitchWaterFluid FLOWING = new WitchWaterFluid(false);
    public static final WitchWaterBlock BLOCK = new WitchWaterBlock(STILL, ModFluids.BLOCK_SETTINGS);

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == STILL || fluid == FLOWING;
    }

    public WitchWaterFluid(boolean isStill) {
        super(isStill, FLUID_SETTINGS,
                () -> BLOCK,
                () -> BUCKET,
                () -> FLOWING,
                () -> STILL
        );
    }

}
