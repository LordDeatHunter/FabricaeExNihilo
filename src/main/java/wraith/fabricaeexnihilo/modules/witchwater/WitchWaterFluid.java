package wraith.fabricaeexnihilo.modules.witchwater;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.modules.ModFluids;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;
import wraith.fabricaeexnihilo.modules.base.FluidSettings;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class WitchWaterFluid extends AbstractFluid {

    private static final FluidSettings FLUID_SETTINGS = new FluidSettings("witchwater", 0x311362, false);
    public static final WitchWaterFluid FLOWING = new WitchWaterFluid(false);
    public static final WitchWaterFluid STILL = new WitchWaterFluid(true);
    public static final BucketItem BUCKET = new BucketItem(STILL, ModFluids.BUCKET_ITEM_SETTINGS);
    public static final WitchWaterBlock BLOCK = new WitchWaterBlock(STILL, FabricBlockSettings.copyOf(ModFluids.BLOCK_SETTINGS));
    public static final TagKey<Fluid> TAG = TagKey.of(Registry.FLUID_KEY, id("witchwater"));

    public WitchWaterFluid(boolean isStill) {
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

}
