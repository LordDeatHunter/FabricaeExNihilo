package wraith.fabricaeexnihilo.modules.fluids;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.ModFluids;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;
import wraith.fabricaeexnihilo.modules.base.BaseFluidBlock;
import wraith.fabricaeexnihilo.modules.base.FluidSettings;

public class MilkFluid extends AbstractFluid {

    private static final FluidSettings FLUID_SETTINGS = new FluidSettings("milk", false);
    public static final Tag.Identified<Fluid> TAG = TagFactory.FLUID.create(new Identifier("c:milk"));
    public static final MilkFluid STILL = new MilkFluid(true);
    public static final MilkFluid FLOWING = new MilkFluid(false);
    public static final BaseFluidBlock BLOCK = new BaseFluidBlock(STILL, ModFluids.BLOCK_SETTINGS);
    public static final Item BUCKET = Items.MILK_BUCKET;

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == STILL || fluid == FLOWING;
    }

    public MilkFluid(boolean isStill) {
        super(isStill, FLUID_SETTINGS,
                () -> BLOCK,
                () -> Items.MILK_BUCKET,
                () -> FLOWING,
                () -> STILL
        );
    }
}
