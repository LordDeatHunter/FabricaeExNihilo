package wraith.fabricaeexnihilo.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

public abstract class BaseRecipe<I extends Inventory> implements Recipe<I> {
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
    public ItemStack craft(I inventory) {
        return getOutput().copy();
    }
}
