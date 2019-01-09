package exnihilocreatio.tiles;

import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ItemHandlerAutoSifter extends ItemStackHandler {
    @Setter
    TileAutoSifter te;

    public ItemHandlerAutoSifter() {
        super(1);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (ExNihiloRegistryManager.SIEVE_REGISTRY.canBeSifted(stack)) {
            return super.insertItem(slot, stack, simulate);
        } else {
            return stack;
        }
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

    @Override
    protected void onContentsChanged(int slot) {
        te.markDirtyClient();
    }
}
