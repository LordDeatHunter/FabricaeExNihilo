package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.barrels.modes.FluidMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.ItemMode;
import wraith.fabricaeexnihilo.recipe.barrel.FluidCombinationRecipe;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public class FluidOnTopDisplay implements Display {

    private final EntryIngredient barrel;
    private final List<EntryIngredient> blockAbove;
    private final List<EntryIngredient> fluidInside;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;

    public FluidOnTopDisplay(FluidCombinationRecipe recipe) {
        this.barrel = EntryIngredients.of(ItemUtils.getExNihiloItemStack("oak_barrel"));

        this.fluidInside = recipe.getContained().flatten(EntryIngredients::of);
        this.blockAbove = recipe.getOther().flatten(EntryIngredients::of);
        this.outputs = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.inputs.addAll(this.fluidInside);
        this.inputs.addAll(this.blockAbove);

        var result = recipe.getResult();
        if (result instanceof ItemMode itemMode) {
            this.outputs.add(EntryIngredients.of(itemMode.getStack()));
        } else if (result instanceof FluidMode fluidMode) {
            var fluid = fluidMode.getFluid().getFluid();
            if (fluid != null) {
                this.outputs.add(EntryIngredients.of(fluid));
            } else {
                this.outputs.add(EntryIngredient.empty());
            }
        } else {
            this.outputs.add(EntryIngredient.empty());
        }
    }

    public EntryIngredient getBarrel() {
        return barrel;
    }

    public List<EntryIngredient> getBlockAbove() {
        return blockAbove;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.FLUID_ABOVE;
    }

    public List<EntryIngredient> getFluidInside() {
        return fluidInside;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return this.inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return this.outputs;
    }
}