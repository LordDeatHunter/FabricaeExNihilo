package wraith.fabricaeexnihilo.compatibility.rei.tools;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.recipe.ToolRecipe;

import java.util.Collections;
import java.util.List;

public class ToolDisplay implements Display {

    private final CategoryIdentifier<ToolDisplay> category;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;

    public ToolDisplay(ToolRecipe recipe, CategoryIdentifier<ToolDisplay> category) {
        this.category = category;
        this.inputs = recipe.getBlock().streamEntries().map(EntryIngredients::of).toList();
        //TODO: Add multiple outputs
        this.outputs = Collections.singletonList(EntryIngredients.of(recipe.getOutput()));
    }

    @Override
    public CategoryIdentifier<ToolDisplay> getCategoryIdentifier() {
        return category;
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
