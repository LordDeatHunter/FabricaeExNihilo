package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.barrel.CompostRecipe;

import java.util.Collections;
import java.util.List;

public class CompostDisplay implements Display {

    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;

    public CompostDisplay(CompostRecipe recipe) {
        this.inputs = recipe.getInput().streamEntries().map(EntryIngredients::of).toList();
        this.outputs = Collections.singletonList(EntryIngredients.of(recipe.getResult()));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.COMPOSTING;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return outputs;
    }

}