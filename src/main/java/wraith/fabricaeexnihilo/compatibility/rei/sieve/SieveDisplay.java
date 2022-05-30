package wraith.fabricaeexnihilo.compatibility.rei.sieve;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.SieveRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SieveDisplay implements Display {

    private final List<EntryIngredient> block;
    private final Map<Identifier, ? extends List<Double>> chancesForMesh;
    private final List<EntryIngredient> fluid;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;

    public SieveDisplay(SieveRecipe recipe) {
        this.block = recipe.getInput().flatten(EntryIngredients::of);
        this.outputs = Collections.singletonList(EntryIngredients.of(recipe.getResult()));
        this.fluid = recipe.getFluid().flatten(EntryIngredients::of);
        this.inputs = new ArrayList<>();
        this.inputs.addAll(this.block);
        this.inputs.addAll(this.fluid);
        this.chancesForMesh = recipe.getRolls();
    }

    public List<EntryIngredient> getBlock() {
        return block;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.SIFTING;
    }

    public Map<Identifier, ? extends List<Double>> getChancesForMeshes() {
        return chancesForMesh;
    }

    public List<EntryIngredient> getFluid() {
        return fluid;
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