package wraith.fabricaeexnihilo.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
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
    public final ItemStack craft(T inventory) {
        return getOutput().copy();
    }
    
    @Override
    public final ItemStack getOutput() {
        return getDisplayStack();
    }
    
    @Override
    public abstract boolean matches(T context, World world);
    
    /**
     * Get an itemstack that represents the result of this recipe
     * @return An itemstack representing the output of the recipe. Only used for visuals.
     */
    public abstract ItemStack getDisplayStack();
    
}
