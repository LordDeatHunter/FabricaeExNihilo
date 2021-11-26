package wraith.fabricaeexnihilo.compatibility.rei.sieve;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.api.recipes.SieveRecipe;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.ModBlocks;

import java.util.List;
import java.util.stream.Stream;

public record SieveDisplay(SieveRecipe recipe) implements Display {

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.SIEVE;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return recipe.loot().stream().map(loot -> EntryIngredients.of(loot.getStack())).toList();
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        var sievable = recipe.sievable().asREIEntries();
        var mesh = recipe.mesh().asREIEntries();
        var buckets = recipe.fluid().asREIEntries();
        var sieves = ModBlocks.SIEVES.values().stream().map(EntryIngredients::of).toList();
        return Stream.of(sievable, mesh, buckets, sieves).flatMap(List::stream).toList();
    }

}