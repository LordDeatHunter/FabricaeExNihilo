package wraith.fabricaeexnihilo.compatibility.rei.witchwater;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterWorldRecipe;

import java.util.List;

public class WitchWaterWorldDisplay implements Display {

    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;

    public WitchWaterWorldDisplay(WitchWaterWorldRecipe recipe) {
        this.inputs = recipe.getTarget().streamEntries().map(EntryIngredients::of).toList();
        this.outputs = recipe.getResult().flatten(EntryIngredients::of);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.WITCH_WATER_WORLD;
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
