package wraith.fabricaeexnihilo.modules.fluids;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.WorldView;
import wraith.fabricaeexnihilo.modules.ModFluids;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;
import wraith.fabricaeexnihilo.modules.base.BaseFluidBlock;
import wraith.fabricaeexnihilo.modules.base.FluidSettings;

public class BloodFluid extends AbstractFluid {

    private static final FluidSettings FLUID_SETTINGS = new FluidSettings("blood", 0x6F0000, false, false);
    public static final BloodFluid FLOWING = new BloodFluid(false);
    public static final BloodFluid STILL = new BloodFluid(true);
    public static final BucketItem BUCKET = new BucketItem(STILL, ModFluids.getBucketItemSettings());
    public static final BaseFluidBlock BLOCK = new BaseFluidBlock(STILL, FabricBlockSettings.copyOf(ModFluids.getBlockSettings()));
    public static final TagKey<Fluid> TAG = TagKey.of(Registries.FLUID.getKey(), new Identifier("c:blood"));

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