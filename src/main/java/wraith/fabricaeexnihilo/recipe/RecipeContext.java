package wraith.fabricaeexnihilo.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

/**
 * Shared base for all of our recipe "inputs". These are not real inventories: each recipe type
 * carries its own small record of match parameters (an item, a block state, a fluid, ...) and
 * tests them itself in {@code matches()}.
 * <p>
 * 1.21.11 changed {@code RecipeManager.getFirstMatch} to short-circuit on
 * {@link RecipeInput#isEmpty()}: if the input claims to be empty, the lookup returns an empty
 * stream without ever calling {@code matches}. Leaving the inherited {@code true} therefore made
 * every {@code getFirstMatch} based lookup fail silently - crucibles would not accept any item,
 * heat sources never registered and barrels never matched - while sieve lookups kept working
 * because {@code getAllMatches} walks the recipe list itself and skips that check.
 * <p>
 * A context always represents a concrete thing to match against, so it is never "empty".
 */
public interface RecipeContext extends RecipeInput {
    @Override
    default ItemStack getStackInSlot(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    default int size() {
        return 1;
    }

    @Override
    default boolean isEmpty() {
        return false;
    }
}
