package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.barrel.MilkingRecipe;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public final class MilkingDisplay implements Display {

    private final long amount;
    private final EntryIngredient barrel;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;

    public MilkingDisplay(MilkingRecipe recipe) {
        this.inputs = recipe.getEntity().flattenListOfEggStacks(EntryIngredients::of);
        this.outputs = new ArrayList<>();
        var fluid = recipe.getFluid().getFluid();
        this.outputs.add(fluid != null ? EntryIngredients.of(fluid) : EntryIngredient.empty());
        this.barrel = EntryIngredients.of(ItemUtils.getExNihiloItemStack("oak_barrel"));
        this.amount = recipe.getAmount();
    }

    public long getAmount() {
        return amount;
    }

    public EntryIngredient getBarrel() {
        return this.barrel;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.MILKING;
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