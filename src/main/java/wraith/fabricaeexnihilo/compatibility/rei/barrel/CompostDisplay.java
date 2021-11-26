package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public record CompostDisplay(REICompostRecipe recipe) implements Display {

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
                return PluginEntry.COMPOSTING;
        }

        @Override
        public List<EntryIngredient> getInputEntries() {
                var inputs = new ArrayList<EntryIngredient>();
                inputs.addAll(recipe.reiInputs());
                inputs.addAll(ModBlocks.BARRELS.values().stream().map(EntryIngredients::of).toList());
                return inputs;
        }

        @Override
        public List<EntryIngredient> getOutputEntries() {
                return recipe.reiOutput();
        }


}