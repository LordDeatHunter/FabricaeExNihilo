package exnihilocreatio.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

/**
 * This class wraps an ItemStack in an IItemHandler to allow code to be unified.
 */
public class ItemStackItemHandler implements IItemHandler {
    private ItemStack wrappedStack;

    public ItemStackItemHandler(ItemStack stack){
        this.wrappedStack = stack;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot == 0 ? wrappedStack : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(slot != 0)
            return stack.copy();
        ItemStack returnStack = stack.copy();
        if(ItemStack.areItemStacksEqual(wrappedStack, returnStack) && wrappedStack.isStackable()){
            int toAdd = Math.min(returnStack.getCount(), wrappedStack.getMaxStackSize() - wrappedStack.getCount());
            if(!simulate)
                wrappedStack.grow(toAdd);
            returnStack.shrink(toAdd);
        }
        return returnStack.getCount() > 0 ? returnStack : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(slot!=0 || amount <= 0)
            return ItemStack.EMPTY;
        ItemStack returnStack = wrappedStack.copy();
        returnStack.setCount(Math.min(amount, wrappedStack.getCount()));
        if(!simulate)
            wrappedStack.shrink(returnStack.getCount());
        if(wrappedStack.getCount() == 0)
            wrappedStack = ItemStack.EMPTY;
        return returnStack;
    }

    @Override
    public int getSlotLimit(int slot) {
        return wrappedStack.getMaxStackSize();
    }
}
