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
    private final List<EntryIngredient> output;
    private final List<EntryIngredient> fluidInside;
    private final List<EntryIngredient> blockAbove;
    private final List<EntryIngredient> inputs;

    public FluidOnTopDisplay(FluidCombinationRecipe recipe) {
        this.barrel = EntryIngredients.of(ItemUtils.getExNihiloItemStack("oak_barrel"));

        this.fluidInside = recipe.getContained().flatten(EntryIngredients::of);
        this.blockAbove = recipe.getOther().flatten(EntryIngredients::of);
        this.output = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.inputs.addAll(this.fluidInside);
        this.inputs.addAll(this.blockAbove);

        var result = recipe.getResult();
        if (result instanceof ItemMode itemMode) {
            this.output.add(EntryIngredients.of(itemMode.getStack()));
        } else if (result instanceof FluidMode fluidMode) {
            var fluid = fluidMode.getFluid().getFluid();
            if (fluid != null) {
                this.output.add(EntryIngredients.of(fluid));
            } else {
                this.output.add(EntryIngredient.empty());
            }
        } else {
            this.output.add(EntryIngredient.empty());
        }
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.FLUID_ABOVE;
    }

    public EntryIngredient getBarrel() {
        return barrel;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return this.output;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return this.inputs;
    }

    public List<EntryIngredient> getFluidInside() {
        return fluidInside;
    }

    public List<EntryIngredient> getBlockAbove() {
        return blockAbove;
    }
}