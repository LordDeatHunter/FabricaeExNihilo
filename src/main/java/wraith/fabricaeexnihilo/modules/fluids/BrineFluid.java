package wraith.fabricaeexnihilo.modules.fluids;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.modules.ModFluids;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;
import wraith.fabricaeexnihilo.modules.base.BaseFluidBlock;
import wraith.fabricaeexnihilo.modules.base.FluidSettings;

public class BrineFluid extends AbstractFluid {
    
    private static final FluidSettings FLUID_SETTINGS = new FluidSettings("brine", false);
    public static final TagKey<Fluid> TAG = TagKey.of(Registry.FLUID_KEY, new Identifier("c:brine"));
    public static final BrineFluid STILL = new BrineFluid(true);
    public static final BucketItem BUCKET = new BucketItem(STILL, ModFluids.BUCKET_ITEM_SETTINGS);
    public static final BrineFluid FLOWING = new BrineFluid(false);
    public static final BaseFluidBlock BLOCK = new BaseFluidBlock(STILL, FabricBlockSettings.copyOf(ModFluids.BLOCK_SETTINGS));
    
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
