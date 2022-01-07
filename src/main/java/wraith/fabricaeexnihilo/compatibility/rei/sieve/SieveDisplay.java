package wraith.fabricaeexnihilo.compatibility.rei.sieve;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.SieveRecipe;

import java.util.List;

public record SieveDisplay(SieveRecipe recipe) implements Display {
    
    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.SIEVE;
    }
    
    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of();
        // TODO: not easy fix: we need to reformat these...
        //return recipe.getRolls().entrySet().stream().map(loot -> EntryIngredients.of(loot.)).toList();
    }
    
    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of();
        // TODO: not easy fix: we need to reformat these...
        /*
        var sievable = recipe.sievable().asREIEntries();
        var mesh = recipe.mesh().asREIEntries();
        var buckets = recipe.fluid().asREIEntries();
        var sieves = ModBlocks.SIEVES.values().stream().map(EntryIngredients::of).toList();
        return Stream.of(sievable, mesh, buckets, sieves).flatMap(List::stream).toList();
        
         */
    }
    
}