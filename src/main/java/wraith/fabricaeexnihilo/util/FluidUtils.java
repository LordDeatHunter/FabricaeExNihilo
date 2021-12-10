package wraith.fabricaeexnihilo.util;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class FluidUtils {

    private FluidUtils() {}

    public static FluidState getDefaultFluidState(FluidBlock fluidBlock) {
        return fluidBlock.getFluidState(fluidBlock.getDefaultState());
    }

    public static Fluid getFluid(FluidBlock fluidBlock) {
        return getDefaultFluidState(fluidBlock).getFluid();
    }

    public static FluidVolume asVolume(Fluid fluid) {
        return FluidVolume.create(fluid, FluidVolume.BUCKET);
    }

    public static FluidVolume proxyFluidVolume(IBucketItem bucket, ItemStack stack) {
        return bucket.libblockattributes__getFluid(stack).isEmpty()
                ? FluidKeys.EMPTY.withAmount(FluidAmount.of1620(0))
                : bucket.libblockattributes__getFluid(stack).withAmount(bucket.libblockattributes__getFluidVolumeAmount());
    }

    public static FluidVolume copyLess(FluidVolume fluidVolume, FluidAmount amount) {
        return amount.isGreaterThanOrEqual(fluidVolume.amount())
                ? FluidKeys.EMPTY.withAmount(FluidAmount.of1620(0))
                : fluidVolume.withAmount(fluidVolume.amount().safeSub(amount).roundedResult);
    }

    public static FluidVolume fromID(FluidVolume fluidVolume, Identifier identifier, int amount) {
        return FluidKeys.get(Registry.FLUID.get(identifier)).withAmount(amount);
    }

}
