package wraith.fabricaeexnihilo.compatibility.rei.crucible;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleRecipe;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class CrucibleDisplay implements Display {

    private final long amount;
    private final CategoryIdentifier<?> category;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;

    public CrucibleDisplay(CrucibleRecipe recipe, CategoryIdentifier<?> category) {
        this.inputs = recipe.getInput().flatten(EntryIngredients::of);
        this.category = category;
        this.outputs = Collections.singletonList(EntryIngredients.of(recipe.getFluid().getFluid()));
        this.amount = recipe.getAmount();
    }

    public long getAmount() {
        return amount;
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

}