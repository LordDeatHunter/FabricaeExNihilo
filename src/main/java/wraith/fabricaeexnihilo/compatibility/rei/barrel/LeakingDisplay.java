package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.barrel.LeakingRecipe;

import java.util.ArrayList;
import java.util.List;

public class LeakingDisplay implements Display {

    private final List<EntryIngredient> block;
    private final List<EntryIngredient> fluid;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;
    private final long amount;

    public LeakingDisplay(LeakingRecipe recipe) {
        this.block = recipe.getBlock().flatten(EntryIngredients::of);
        this.fluid = recipe.getFluid().flatten(EntryIngredients::of);
        this.inputs = new ArrayList<>();
        this.inputs.addAll(this.block);
        this.inputs.addAll(this.fluid);
        this.outputs = new ArrayList<>();
        this.outputs.add(EntryIngredients.of(recipe.getResult()));
        this.amount = recipe.getAmount();
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return this.inputs;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return this.outputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.LEAKING;
    }

    public List<EntryIngredient> getBlock() {
        return block;
    }

    public List<EntryIngredient> getFluid() {
        return fluid;
    }
}
