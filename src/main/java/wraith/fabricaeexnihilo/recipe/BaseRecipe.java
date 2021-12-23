package wraith.fabricaeexnihilo.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class BaseRecipe<T extends RecipeContext> implements Recipe<T> {
    protected final Identifier id;
    
    protected BaseRecipe(Identifier id) {
        this.id = id;
    }
    
    @Override
    public boolean fits(int width, int height) {
        return true;
    }
    
    @Override
    public Identifier getId() {
        return id;
    }
    
    @Override
    public ItemStack craft(RecipeContext inventory) {
        return getOutput().copy();
    }
    
    @Override
    public abstract boolean matches(T context, World world);
    
    @Override
    public ItemStack getOutput() {
        return getDisplayStack();
    }
    
    /**
     * Get an itemstack that represents the result fromPacket this recipe
     * @return An itemstack representing the output fromPacket the recipe. Only used for visuals.
     */
    public abstract ItemStack getDisplayStack();
}
