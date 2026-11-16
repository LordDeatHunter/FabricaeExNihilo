package wraith.fabricaeexnihilo.datagen.provider.tag;

import net.minecraft.registry.tag.TagKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.Registries;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.fluid.Fluid;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.modules.fluids.BloodFluid;
import wraith.fabricaeexnihilo.modules.fluids.BrineFluid;
import wraith.fabricaeexnihilo.modules.fluids.MilkFluid;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;

import java.util.concurrent.CompletableFuture;

public class FluidTagProvider extends FabricTagProvider.FluidTagProvider {

    public FluidTagProvider(FabricDataOutput generator, CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(generator, registries);
    }

    /** 1.21.11 tag builders take RegistryKeys, so map the objects we pass in. */
    private ProvidedTagBuilder<Fluid, Fluid> tag(TagKey<Fluid> key) {
        return builder(key).mapped(v -> Registries.FLUID.getKey(v).orElseThrow());
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // Generate dummies to avoid datagen errors (minecraft/fapi specify at runtime)
        tag(ConventionalFluidTags.LAVA);

        tag(ModTags.TRUE_LAVA)
                .add(Fluids.LAVA, Fluids.FLOWING_LAVA);
        tag(ModTags.TRUE_WATER)
                .add(Fluids.WATER, Fluids.FLOWING_WATER);
        tag(ModTags.Common.BRINE)
                .add(BrineFluid.FLOWING, BrineFluid.STILL);
        tag(ModTags.Common.BLOOD)
                .add(BloodFluid.FLOWING, BloodFluid.STILL);
        tag(ModTags.WITCHWATER)
                .add(WitchWaterFluid.FLOWING, WitchWaterFluid.STILL);
        tag(ModTags.HOT)
                .addTag(ConventionalFluidTags.LAVA)
                .addOptionalTag(TagKey.of(RegistryKeys.FLUID, Identifier.of("techreborn", "nitro_diesel")));
        tag(ConventionalFluidTags.MILK)
                .add(MilkFluid.FLOWING, MilkFluid.STILL);
        tag(ModTags.WATER_LIKE)
                .add(BrineFluid.FLOWING, BrineFluid.STILL,
                        BloodFluid.FLOWING, BloodFluid.STILL,
                        WitchWaterFluid.FLOWING, WitchWaterFluid.STILL,
                        MilkFluid.FLOWING, MilkFluid.STILL);
    }
}
