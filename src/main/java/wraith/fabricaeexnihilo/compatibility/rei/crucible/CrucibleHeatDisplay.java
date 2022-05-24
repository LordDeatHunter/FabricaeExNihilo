package wraith.fabricaeexnihilo.compatibility.rei.crucible;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleHeatRecipe;

import java.util.ArrayList;
import java.util.List;

public class CrucibleHeatDisplay implements Display {

    private final List<EntryIngredient> inputs;
    private final int heat;

    public CrucibleHeatDisplay(CrucibleHeatRecipe recipe) {
        this.inputs = recipe.getBlock().flatten(EntryIngredients::of);
        this.heat = recipe.getHeat();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.HEATING;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return new ArrayList<>();
    }

    public int getHeat() {
        return heat;
    }

}