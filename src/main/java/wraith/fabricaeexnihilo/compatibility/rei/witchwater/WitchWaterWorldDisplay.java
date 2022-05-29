package wraith.fabricaeexnihilo.compatibility.rei.witchwater;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterWorldRecipe;

import java.util.List;

public class WitchWaterWorldDisplay implements Display {

    private final List<EntryIngredient> input;
    private final List<EntryIngredient> output;

    public WitchWaterWorldDisplay(WitchWaterWorldRecipe recipe) {
        this.input = recipe.getTarget().flatten(EntryIngredients::of);
        this.output = recipe.getResult().flatten(EntryIngredients::of);
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return input;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return output;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.WITCH_WATER_WORLD;
    }

}
