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

    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> output;
    private final EntryIngredient barrel;
    private final long amount;

    public MilkingDisplay(MilkingRecipe recipe) {
        this.inputs = recipe.getEntity().flattenListOfEggStacks(EntryIngredients::of);
        this.output = new ArrayList<>();
        var fluid = recipe.getFluid().getFluid();
        this.output.add(fluid != null ? EntryIngredients.of(fluid) : EntryIngredient.empty());
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
        return output;
    }

}