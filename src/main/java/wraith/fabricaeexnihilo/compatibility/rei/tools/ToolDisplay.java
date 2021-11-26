package wraith.fabricaeexnihilo.compatibility.rei.tools;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.api.recipes.ToolRecipe;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.ModTools;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public record ToolDisplay(ToolRecipe recipe, CategoryIdentifier<ToolDisplay> category) implements Display {

    @Override
    public CategoryIdentifier<ToolDisplay> getCategoryIdentifier() {
        return category;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return Collections.singletonList(EntryIngredient.of(recipe.lootables()
                .stream().map(loot -> ItemUtils.asREIEntry(loot.getStack()))
                .toList())
        );
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        var ingredients = recipe.ingredient().asREIEntries();
        var tools = (category == PluginEntry.HAMMER ? ModTools.HAMMERS : ModTools.CROOKS)
                .values().stream()
                .map(EntryIngredients::of)
                .toList();
        return Stream.of(ingredients, tools).flatMap(List::stream).toList();
    }

}
