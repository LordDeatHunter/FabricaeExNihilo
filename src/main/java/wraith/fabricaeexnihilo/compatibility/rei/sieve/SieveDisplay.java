package wraith.fabricaeexnihilo.compatibility.rei.sieve;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.SieveRecipe;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SieveDisplay implements Display {

    private final List<EntryIngredient> input;
    private final List<EntryIngredient> output;
    private final List<EntryIngredient> fluid;
    private final Map<Identifier, ? extends List<Double>> chancesForMesh;

    public SieveDisplay(SieveRecipe recipe) {
        this.input = recipe.getInput().flatten(EntryIngredients::of);
        this.output = Collections.singletonList(EntryIngredients.of(recipe.getResult()));
        this.fluid = recipe.getFluid().flatten(EntryIngredients::of);
        this.chancesForMesh = recipe.getRolls();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.SIFTING;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return output;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return input;
    }

    public List<EntryIngredient> getFluid() {
        return fluid;
    }

    public Map<Identifier, ? extends List<Double>> getChancesForMeshes() {
        return chancesForMesh;
    }

}