package exnihilocreatio.tiles;

import exnihilocreatio.registries.CrucibleRegistryBase;
import exnihilocreatio.registries.types.Meltable;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CrucibleItemHandler<R extends CrucibleRegistryBase> extends ItemStackHandler {

    @Setter
    protected TileCrucibleBase te;

    public CrucibleItemHandler() {
        super(1);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (R.canBeMelted(stack)) {
            Meltable meltable = R.getMeltable(stack);
            if ((meltable.getAmount() + meltable.getAmount() * getStackInSlot(0).getCount() + te.getSolidAmount()) <= (meltable.getAmount() * TileCrucibleBase.MAX_ITEMS)) {
                return super.insertItem(slot, stack, simulate);
            }
        }

        return stack;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }
}
