package wraith.fabricaeexnihilo.recipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import wraith.fabricaeexnihilo.util.EmptyInventory;

public interface RecipeContext extends EmptyInventory {
    /**
     * @deprecated Not intended for direct use
     */
    @Deprecated
    @Override
    default int size() {
        return EmptyInventory.super.size();
    }

    /**
     * @deprecated Not intended for direct use
     */
    @Deprecated
    @Override
    default boolean isEmpty() {
        return EmptyInventory.super.isEmpty();
    }

    /**
     * @deprecated Not intended for direct use
     */
    @Deprecated
    @Override
    default ItemStack getStack(int slot) {
        return EmptyInventory.super.getStack(slot);
    }

    /**
     * @deprecated Not intended for direct use
     */
    @Deprecated
    @Override
    default ItemStack removeStack(int slot, int amount) {
        return EmptyInventory.super.removeStack(slot, amount);
    }

    /**
     * @deprecated Not intended for direct use
     */
    @Deprecated
    @Override
    default ItemStack removeStack(int slot) {
        return EmptyInventory.super.removeStack(slot);
    }

    /**
     * @deprecated Not intended for direct use
     */
    @Deprecated
    @Override
    default void setStack(int slot, ItemStack stack) {
        EmptyInventory.super.setStack(slot, stack);
    }

    /**
     * @deprecated Not intended for direct use
     */
    @Deprecated
    @Override
    default void markDirty() {
        EmptyInventory.super.markDirty();
    }

    /**
     * @deprecated Not intended for direct use
     */
    @Deprecated
    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return EmptyInventory.super.canPlayerUse(player);
    }

    /**
     * @deprecated Not intended for direct use
     */
    @Deprecated
    @Override
    default void clear() {
        EmptyInventory.super.clear();
    }
}