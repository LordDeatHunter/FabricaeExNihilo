package exnihilocreatio.tiles;

import exnihilocreatio.registries.CrucibleRegistry;
import exnihilocreatio.registries.types.Meltable;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CrucibleItemHandler extends ItemStackHandler {

    @Setter
    private TileCrucible te;

    public CrucibleItemHandler() {
        super(1);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (CrucibleRegistry.canBeMelted(stack)) {
            Meltable meltable = CrucibleRegistry.getMeltable(stack);
            if ((meltable.getAmount() + meltable.getAmount() * getStackInSlot(0).getCount() + te.getSolidAmount()) <= (meltable.getAmount() * TileCrucible.MAX_ITEMS)) {
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
