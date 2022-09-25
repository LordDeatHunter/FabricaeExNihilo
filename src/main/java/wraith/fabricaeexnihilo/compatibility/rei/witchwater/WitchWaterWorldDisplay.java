package wraith.fabricaeexnihilo.compatibility.rei.witchwater;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterWorldRecipe;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.List;

public class WitchWaterWorldDisplay implements Display {
    public final EntryIngredient input;
    public final List<EntryIngredient> outputs;

    public WitchWaterWorldDisplay(WitchWaterWorldRecipe recipe) {
        this.input = RegistryEntryLists.asReiIngredient(recipe.getTarget(), EntryStacks::of);
        this.outputs = recipe.getResult().flatten(EntryIngredients::of);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.WITCH_WATER_WORLD;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(input);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return outputs;
    }

}
