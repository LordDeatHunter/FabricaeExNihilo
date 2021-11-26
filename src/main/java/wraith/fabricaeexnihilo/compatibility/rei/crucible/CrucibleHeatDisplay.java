package wraith.fabricaeexnihilo.compatibility.rei.crucible;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.api.recipes.crucible.CrucibleHeatRecipe;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public record CrucibleHeatDisplay(CrucibleHeatRecipe recipe) implements Display {

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.CRUCIBLE_HEAT;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        var recipes = Stream.of(recipe.ingredient().asREIEntries(), recipe.fluid().asREIEntries()).flatMap(List::stream).toList();
        var crucible = Collections.singletonList(EntryIngredients.of(ItemUtils.getExNihiloItemStack("stone_crucible")));
        return Stream.of(recipes, crucible).flatMap(List::stream).toList();
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return new ArrayList<>();
    }

    public int getHeat() {
        return recipe.value();
    }

}