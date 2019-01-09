package exnihilocreatio.tiles;

import exnihilocreatio.registries.registries.CrucibleRegistry;
import exnihilocreatio.registries.types.Meltable;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CrucibleItemHandler extends ItemStackHandler {

    protected final TileCrucibleBase te;
    protected final CrucibleRegistry crucibleRegistry;

    public CrucibleItemHandler(TileCrucibleBase te, CrucibleRegistry crucibleRegistry) {
        super(1);
        this.te = te;
        this.crucibleRegistry = crucibleRegistry;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (crucibleRegistry.canBeMelted(stack)) {
            Meltable meltable = crucibleRegistry.getMeltable(stack);
            int totalSolidAmount = meltable.getAmount() + meltable.getAmount() * getStackInSlot(0).getCount() + te.getSolidAmount();
            int allowedSolidAmount = meltable.getAmount() * TileCrucibleBase.MAX_ITEMS;

            if (totalSolidAmount <= allowedSolidAmount) {
                te.currentItem = new ItemInfo(stack);

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

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return te.solidAmount > 0 ? TileCrucibleBase.MAX_ITEMS - 1 : TileCrucibleBase.MAX_ITEMS;
    }

    @Override
    protected void onContentsChanged(int slot) {
        te.markDirtyClient();
    }
}
