package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.barrels.modes.FluidMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.ItemMode;
import wraith.fabricaeexnihilo.recipe.barrel.FluidTransformationRecipe;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public record TransformingDisplay(FluidTransformationRecipe recipe) implements Display {
    
    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.TRANSFORMING;
    }
    
    @Override
    public List<EntryIngredient> getInputEntries() {
        var inBarrel = recipe.getContained().asREIEntries();
        var catalyst = recipe.getCatalyst().asREIEntries();
        var barrels = ModBlocks.BARRELS.values().stream().map(EntryIngredients::of).toList();
        return Stream.of(inBarrel, catalyst, barrels).flatMap(List::stream).toList();
    }
    
    @Override
    public List<EntryIngredient> getOutputEntries() {
        var result = recipe.getResult();
        if (result instanceof ItemMode itemMode) {
            return Collections.singletonList(EntryIngredients.of(itemMode.getStack()));
        } else if (result instanceof FluidMode fluidMode) {
            var fluid = fluidMode.getFluid().getFluid();
            if (fluid != null) {
                var bucket = fluid.getBucketItem();
                if (bucket != null) {
                    return Collections.singletonList(EntryIngredients.of(bucket));
                }
            }
        }
        return Collections.singletonList(EntryIngredient.empty());
    }
    
}