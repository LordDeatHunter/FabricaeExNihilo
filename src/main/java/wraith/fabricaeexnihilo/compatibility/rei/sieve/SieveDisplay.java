package wraith.fabricaeexnihilo.compatibility.rei.sieve;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SieveDisplay implements Display {

    private final List<EntryIngredient> blocks;
    private final List<EntryIngredient> fluids;
    private final List<EntryIngredient> inputs;
    private final EntryIngredient mesh;
    private final HashMap<EntryIngredient, List<Double>> outputChances;
    private final List<EntryIngredient> outputs;


    public SieveDisplay(SieveRecipeHolder recipeHolder) {
        this.blocks = recipeHolder.getInputs();
        this.fluids = recipeHolder.getFluids();
        this.mesh = recipeHolder.getMesh();
        this.inputs = new ArrayList<>();
        this.inputs.addAll(this.blocks);
        this.inputs.addAll(this.fluids);
        this.inputs.add(mesh);
        this.outputChances = new HashMap<>(recipeHolder.getOutputs());
        this.outputs = new ArrayList<>(outputChances.keySet());

    }

    public List<EntryIngredient> getBlocks() {
        return blocks;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.SIFTING;
    }

    public List<EntryIngredient> getFluids() {
        return fluids;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return inputs;
    }

    public EntryIngredient getMesh() {
        return mesh;
    }

    public HashMap<EntryIngredient, List<Double>> getOutputChances() {
        return outputChances;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return outputs;
    }

}