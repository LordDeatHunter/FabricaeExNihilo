package wraith.fabricaeexnihilo.modules;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;
import wraith.fabricaeexnihilo.modules.fluids.BloodFluid;
import wraith.fabricaeexnihilo.modules.fluids.BrineFluid;
import wraith.fabricaeexnihilo.modules.fluids.MilkFluid;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;

import java.util.List;

public final class ModFluids {
    public static final List<AbstractFluid> FLUIDS = List.of(
            WitchWaterFluid.STILL,
            MilkFluid.STILL,
            BrineFluid.STILL,
            BloodFluid.STILL
    );

    public static FabricBlockSettings getBlockSettings() {
        return FabricBlockSettings.copyOf(Blocks.WATER).liquid().replaceable().noCollision();
    }

    public static FabricItemSettings getBucketItemSettings() {
        return new FabricItemSettings().maxCount(1).recipeRemainder(Items.BUCKET);
    }

    public static void registerBuckets() {
        FLUIDS.stream().filter(fluid -> fluid != MilkFluid.STILL).forEach(AbstractFluid::registerBucket);
    }

    public static void registerFluidBlocks() {
        FLUIDS.forEach(AbstractFluid::registerFluidBlock);
    }

    public static void registerFluids() {
        FLUIDS.forEach(AbstractFluid::registerFluids);
    }
}
