package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.recipe.barrel.MilkingRecipe;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public record MilkingDisplay(MilkingRecipe recipe) implements Display {
    
    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.MILKING;
    }
    
    @Override
    public List<EntryIngredient> getInputEntries() {
        var entity = recipe.getEntity().flattenListOfEggStacks(EntryIngredients::of);
        var barrels = ModBlocks.BARRELS.values().stream().map(EntryIngredients::of).toList();
        return Stream.of(entity, barrels).flatMap(List::stream).toList();
    }
    
    @Override
    public List<EntryIngredient> getOutputEntries() {
        var fluid = recipe.getFluid().getFluid();
        if (fluid != null) {
            var bucket = fluid.getBucketItem();
            if (bucket != null) {
                return Collections.singletonList(EntryIngredients.of(bucket));
            }
        }
        return Collections.singletonList(EntryIngredient.empty());
    }
    
}