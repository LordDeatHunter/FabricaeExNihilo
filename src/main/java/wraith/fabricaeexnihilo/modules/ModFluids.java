package wraith.fabricaeexnihilo.modules;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.block.Material;
import net.minecraft.item.Items;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;
import wraith.fabricaeexnihilo.modules.fluids.BloodFluid;
import wraith.fabricaeexnihilo.modules.fluids.BrineFluid;
import wraith.fabricaeexnihilo.modules.fluids.MilkFluid;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;

import java.util.List;

public final class ModFluids {

    public static final FabricItemSettings BUCKET_ITEM_SETTINGS = new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP).maxCount(1).recipeRemainder(Items.BUCKET).maxCount(1);
    public static final FabricBlockSettings BLOCK_SETTINGS = FabricBlockSettings.of(Material.WATER).noCollision().strength(100.0f, 100.0f).dropsNothing();
    
    public static final List<AbstractFluid> FLUIDS = List.of(
            WitchWaterFluid.STILL,
            MilkFluid.STILL,
            BrineFluid.STILL,
            BloodFluid.STILL
    );
    public static final List<AbstractFluid> FLUIDS_FLOWING = List.of(
            WitchWaterFluid.FLOWING,
            MilkFluid.FLOWING,
            BrineFluid.FLOWING,
            BloodFluid.FLOWING
    );

    public static void registerFluids() {
        FLUIDS.forEach(AbstractFluid::registerFluids);
    }

    public static void registerFluidBlocks() {
        FLUIDS.forEach(AbstractFluid::registerFluidBlock);
    }

    public static void registerBuckets() {
        FLUIDS.stream().filter(fluid -> fluid != MilkFluid.STILL).forEach(AbstractFluid::registerBucket);
    }
}
