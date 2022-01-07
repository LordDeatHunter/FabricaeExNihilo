package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.SpawnEggItem;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.barrels.modes.FluidMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.ItemMode;
import wraith.fabricaeexnihilo.recipe.barrel.AlchemyRecipe;

import java.util.ArrayList;
import java.util.List;

public record AlchemyDisplay(AlchemyRecipe recipe) implements Display {
    
    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.ALCHEMY;
    }
    
    @Override
    public List<EntryIngredient> getInputEntries() {
        var inputs = new ArrayList<EntryIngredient>();
        inputs.addAll(recipe.getReactant().asREIEntries());
        inputs.addAll(recipe.getCatalyst().asREIEntries());
        inputs.addAll(ModBlocks.BARRELS.values().stream().map(EntryIngredients::of).toList());
        return inputs;
    }
    
    @Override
    public List<EntryIngredient> getOutputEntries() {
        var outputs = new ArrayList<EntryIngredient>();
        var mode = recipe.getResult();
        if (mode instanceof ItemMode itemMode) {
            outputs.add(EntryIngredients.of(itemMode.getStack()));
        } else if (mode instanceof FluidMode fluidMode) {
            var fluid = fluidMode.getFluid().getFluid();
            if (fluid == null) {
                outputs.add(EntryIngredient.empty());
            } else {
                var bucket = fluid.getBucketItem();
                outputs.add(bucket == null ? EntryIngredient.empty() : EntryIngredients.of(bucket));
            }
        } else {
            outputs.add(EntryIngredient.empty());
        }
        outputs.add(EntryIngredients.of(recipe.getByproduct().stack()));
        outputs.add(!recipe.getToSpawn().isEmpty() ? EntryIngredients.of(SpawnEggItem.forEntity(recipe.getToSpawn().getType())) : EntryIngredient.empty());
        return outputs;
    }
    
}