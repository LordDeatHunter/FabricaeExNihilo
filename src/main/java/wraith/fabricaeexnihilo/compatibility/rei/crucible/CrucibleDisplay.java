package wraith.fabricaeexnihilo.compatibility.rei.crucible;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleRecipe;

import java.util.Collections;
import java.util.List;

public class CrucibleDisplay implements Display {

    private final CategoryIdentifier<?> category;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;
    private final long amount;

    @SuppressWarnings("UnstableApiUsage")
    public CrucibleDisplay(CrucibleRecipe recipe, CategoryIdentifier<?> category) {
        this.inputs = recipe.getInput().flatten(EntryIngredients::of);
        this.category = category;
        this.outputs = Collections.singletonList(EntryIngredients.of(recipe.getFluid().getFluid()));
        this.amount = recipe.getAmount();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return category;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return outputs;
    }

    public long getAmount() {
        return amount;
    }

}