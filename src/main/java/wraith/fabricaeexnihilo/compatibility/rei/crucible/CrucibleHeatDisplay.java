package wraith.fabricaeexnihilo.compatibility.rei.crucible;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.fluid.Fluids;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleHeatRecipe;

import java.util.ArrayList;
import java.util.List;

public class CrucibleHeatDisplay implements Display {

    private final int heat;
    private final List<EntryIngredient> inputs;

    public CrucibleHeatDisplay(CrucibleHeatRecipe recipe) {
        this.inputs = recipe.getBlock().flatten(block -> {
            var fluid = block.getDefaultState().getFluidState().getFluid();
            if (fluid != Fluids.EMPTY) {
                return EntryIngredients.of(fluid);
            }
            return EntryIngredients.of(block);
        });
        this.heat = recipe.getHeat();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.HEATING;
    }

    public int getHeat() {
        return heat;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return new ArrayList<>();
    }

}