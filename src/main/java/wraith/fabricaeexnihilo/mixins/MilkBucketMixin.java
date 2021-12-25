package wraith.fabricaeexnihilo.mixins;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MilkBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import wraith.fabricaeexnihilo.modules.fluids.MilkFluid;

@Mixin(MilkBucketItem.class)
public class MilkBucketMixin extends Item implements IBucketItem {

    public MilkBucketMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean libblockattributes__shouldExposeFluid() {
        return true;
    }

    @Override
    public FluidKey libblockattributes__getFluid(ItemStack itemStack) {
        return FluidKeys.get(MilkFluid.STILL);
    }

    @Override
    public ItemStack libblockattributes__withFluid(FluidKey fluidKey) {
        if (fluidKey == FluidKeys.get(MilkFluid.STILL)) {
            return new ItemStack(Items.MILK_BUCKET);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack libblockattributes__drainedOfFluid(ItemStack stack) {
        return new ItemStack(Items.BUCKET);
    }

    @Override
    public FluidAmount libblockattributes__getFluidVolumeAmount() {
        return FluidAmount.BUCKET;
    }

}
