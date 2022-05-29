package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.barrels.modes.FluidMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.ItemMode;
import wraith.fabricaeexnihilo.recipe.barrel.FluidTransformationRecipe;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class TransformingDisplay implements Display {

    private final EntryIngredient barrel;
    private final List<EntryIngredient> catalyst;
    private final List<EntryIngredient> contained;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;

    public TransformingDisplay(FluidTransformationRecipe recipe) {
        this.barrel = EntryIngredients.of(ItemUtils.getExNihiloItemStack("oak_barrel"));
        this.catalyst = recipe.getCatalyst().flatten(EntryIngredients::of);
        this.contained = recipe.getContained().flatten(EntryIngredients::of);

        this.inputs = new ArrayList<>();
        this.inputs.addAll(this.catalyst);
        this.inputs.addAll(this.contained);

        var result = recipe.getResult();
        this.outputs = new ArrayList<>();
        if (result instanceof ItemMode itemMode) {
            outputs.add(EntryIngredients.of(itemMode.getStack()));
        } else if (result instanceof FluidMode fluidMode) {
            var fluid = fluidMode.getFluid().getFluid();
            if (fluid != null) {
                outputs.add(EntryIngredients.of(fluid));
            } else {
                outputs.add(EntryIngredient.empty());
            }
        } else {
            outputs.add(EntryIngredient.empty());
        }
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.TRANSFORMING;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return this.inputs;
    }

    public EntryIngredient getBarrel() {
        return this.barrel;
    }

    public List<EntryIngredient> getCatalyst() {
        return catalyst;
    }

    public List<EntryIngredient> getContained() {
        return contained;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return this.outputs;
    }

}