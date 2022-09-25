package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.recipe.barrel.FluidCombinationRecipe;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.List;

public class FluidCombinationDisplay implements Display {
    public final BarrelMode result;
    public final EntryIngredient above;
    public final EntryIngredient inside;

    public FluidCombinationDisplay(FluidCombinationRecipe recipe) {
        this.inside = RegistryEntryLists.asReiIngredient(recipe.getContained(), EntryStacks::of);
        this.above = RegistryEntryLists.asReiIngredient(recipe.getOther(), EntryStacks::of);
        this.result = recipe.getResult();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.FLUID_ABOVE;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(inside, above);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(result.getReiResult());
    }
}