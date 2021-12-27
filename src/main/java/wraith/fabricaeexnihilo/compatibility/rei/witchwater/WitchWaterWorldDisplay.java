package wraith.fabricaeexnihilo.compatibility.rei.witchwater;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterWorldRecipe;

import java.util.ArrayList;
import java.util.List;

public record WitchWaterWorldDisplay(WitchWaterWorldRecipe recipe) implements Display {

    @Override
    public List<EntryIngredient> getInputEntries() {
        var list = new ArrayList<EntryIngredient>();
        list.add(EntryIngredients.of(WitchWaterFluid.BUCKET));
        list.addAll(recipe.getTarget().asREIEntries());
        return list;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return recipe.getResult().asEntryList();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.WITCH_WATER_WORLD;
    }

}
