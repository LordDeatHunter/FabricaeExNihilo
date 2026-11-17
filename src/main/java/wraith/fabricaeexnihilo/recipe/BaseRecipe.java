package wraith.fabricaeexnihilo.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public abstract class BaseRecipe<T extends RecipeContext> implements Recipe<T> {

    @Override
    public ItemStack craft(T inventory, RegistryWrapper.WrapperLookup lookup) {
        return getDisplayStack().copy();
    }

    @Override
    public abstract boolean matches(T context, World world);

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.NONE;
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    /**
     * Get an itemstack that represents the result of this recipe
     *
     * @return An itemstack representing the output of the recipe. Only used for visuals.
     */
    public abstract ItemStack getDisplayStack();

}
