package wraith.fabricaeexnihilo.compatibility.rei.crucible;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleHeatRecipe;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.List;

public class CrucibleHeatDisplay implements Display {
    public final int heat;
    public final EntryIngredient source;

    public CrucibleHeatDisplay(CrucibleHeatRecipe recipe) {
        this.source = RegistryEntryLists.asReiIngredient(recipe.getBlock());
        this.heat = recipe.getHeat();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.HEATING;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(source);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of();
    }

}