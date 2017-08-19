package exnihilocreatio.tiles;

import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ItemHandlerGrinder extends ItemStackHandler {

    @Setter
    TileGrinder te;

    public ItemHandlerGrinder() {
        super(1);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return stack;
        /*
        if (SieveRegistry.canBeSifted(stack)) {
            te.markDirtyClient();
            return super.insertItem(slot, stack, simulate);
        }else {
            return stack;
        } TODO: create grind registry
        */
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 32;
    }
}
