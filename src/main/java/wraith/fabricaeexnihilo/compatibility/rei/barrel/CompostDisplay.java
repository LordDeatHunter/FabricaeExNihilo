package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.barrel.CompostRecipe;

import java.util.List;

public class CompostDisplay implements Display {
    public final EntryIngredient input;
    public final EntryIngredient result;

    public CompostDisplay(CompostRecipe recipe) {
        this.input = EntryIngredients.ofIngredient(recipe.getInput());
        this.result = EntryIngredients.of(recipe.getResult());
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.COMPOSTING;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(input);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(result);
    }

}