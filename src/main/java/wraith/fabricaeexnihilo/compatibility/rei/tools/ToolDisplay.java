package wraith.fabricaeexnihilo.compatibility.rei.tools;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.recipe.ToolRecipe;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.List;

//TODO: Make this like sieve, big box of outputs
public class ToolDisplay implements Display {
    public final CategoryIdentifier<ToolDisplay> category;
    public final EntryIngredient block;
    public final EntryIngredient result;

    public ToolDisplay(ToolRecipe recipe, CategoryIdentifier<ToolDisplay> category) {
        this.category = category;
        this.block = RegistryEntryLists.asReiIngredient(recipe.getBlock());
        this.result = EntryIngredients.of(recipe.getResult().stack());
    }

    @Override
    public CategoryIdentifier<ToolDisplay> getCategoryIdentifier() {
        return category;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(block);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(result);
    }

}
