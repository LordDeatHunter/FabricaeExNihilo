package wraith.fabricaeexnihilo.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class BaseRecipe<T extends RecipeContext> implements Recipe<T> {
    protected final Identifier id;

    public BaseRecipe(Identifier id) {
        this.id = id;
    }

    @Override
    public final boolean fits(int width, int height) {
        return true;
    }

    @Override
    public final Identifier getId() {
        return id;
    }

    @Override
    public ItemStack craft(T inventory, DynamicRegistryManager registryManager) {
        return getOutput(registryManager).copy();
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return getDisplayStack();
    }

    @Override
    public abstract boolean matches(T context, World world);

    /**
     * Get an itemstack that represents the result of this recipe
     *
     * @return An itemstack representing the output of the recipe. Only used for visuals.
     */
    public abstract ItemStack getDisplayStack();

}
