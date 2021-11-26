package wraith.fabricaeexnihilo.compatibility.rei.crucible;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.block.Material;
import wraith.fabricaeexnihilo.api.recipes.crucible.CrucibleRecipe;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.ModBlocks;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public record CrucibleDisplay(CrucibleRecipe recipe, CategoryIdentifier<?> category) implements Display {

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return category;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        var inputs = recipe.input().asREIEntries();
        var crucibles = ModBlocks.CRUCIBLES.values().stream().filter(crucible -> category == PluginEntry.WOOD_CRUCIBLE ? crucible.getMaterial() == Material.WOOD : crucible.getMaterial() == Material.STONE).map(EntryIngredients::of).toList();
        return Stream.of(inputs, crucibles).flatMap(List::stream).toList();
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        var fluid = recipe.output().getRawFluid();
        if (fluid != null) {
            var bucket = fluid.getBucketItem();
            if (bucket != null) {
                return Collections.singletonList(EntryIngredients.of(bucket));
            }
        }
        return Collections.singletonList(EntryIngredient.empty());
    }

}