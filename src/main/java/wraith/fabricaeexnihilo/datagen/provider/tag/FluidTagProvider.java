package wraith.fabricaeexnihilo.datagen.provider.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalFluidTags;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.modules.fluids.BloodFluid;
import wraith.fabricaeexnihilo.modules.fluids.BrineFluid;
import wraith.fabricaeexnihilo.modules.fluids.MilkFluid;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;

public class FluidTagProvider extends FabricTagProvider.FluidTagProvider {
    public FluidTagProvider(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    protected void generateTags() {
        // Generate dummies to avoid datagen errors (minecraft/fapi specify at runtime)
        getOrCreateTagBuilder(ConventionalFluidTags.LAVA);

        getOrCreateTagBuilder(ModTags.TRUE_LAVA)
                .add(Fluids.LAVA, Fluids.FLOWING_LAVA);
        getOrCreateTagBuilder(ModTags.TRUE_WATER)
                .add(Fluids.WATER, Fluids.FLOWING_WATER);
        getOrCreateTagBuilder(ModTags.Common.BRINE)
                .add(BrineFluid.FLOWING, BrineFluid.STILL);
        getOrCreateTagBuilder(ModTags.Common.BLOOD)
                .add(BloodFluid.FLOWING, BloodFluid.STILL);
        getOrCreateTagBuilder(ModTags.WITCHWATER)
                .add(WitchWaterFluid.FLOWING, WitchWaterFluid.STILL);
        getOrCreateTagBuilder(ModTags.HOT)
                .addTag(ConventionalFluidTags.LAVA)
                .addOptionalTag(new Identifier("techreborn", "nitro_diesel"));
        getOrCreateTagBuilder(ConventionalFluidTags.MILK)
                .add(MilkFluid.FLOWING, MilkFluid.STILL);
    }
}
