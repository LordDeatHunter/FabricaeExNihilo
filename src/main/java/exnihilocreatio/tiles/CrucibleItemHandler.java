package exnihilocreatio.tiles;

import exnihilocreatio.registries.registries.CrucibleRegistryNew;
import exnihilocreatio.registries.types.Meltable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CrucibleItemHandler extends ItemStackHandler {

    protected TileCrucibleBase te;
    protected CrucibleRegistryNew crucibleRegistry;

    public CrucibleItemHandler(TileCrucibleBase te, CrucibleRegistryNew crucibleRegistry) {
        super(1);
        this.te = te;
        this.crucibleRegistry = crucibleRegistry;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (crucibleRegistry.canBeMelted(stack)) {
            Meltable meltable = crucibleRegistry.getMeltable(stack);
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
