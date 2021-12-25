package wraith.fabricaeexnihilo.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

/**
 * Utility class for an inventory that isn't able to contain any items.
 */
public interface EmptyInventory extends Inventory {
    @Override
    default int size() {
        return 0;
    }
    
    @Override
    default boolean isEmpty() {
        return true;
    }
    
    @Override
    default ItemStack getStack(int slot) {
        throw new UnsupportedOperationException("This inventory is empty!");
    }
    
    @Override
    default ItemStack removeStack(int slot, int amount) {
        throw new UnsupportedOperationException("This inventory is empty!");
    }
    
    @Override
    default ItemStack removeStack(int slot) {
        throw new UnsupportedOperationException("This inventory is empty!");
    }
    
    @Override
    default void setStack(int slot, ItemStack stack) {
        throw new UnsupportedOperationException("This inventory is empty!");
    }
    
    @Override
    default void markDirty() {
    
    }
    
    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return false;
    }
    
    @Override
    default void clear() {
    
    }
}
