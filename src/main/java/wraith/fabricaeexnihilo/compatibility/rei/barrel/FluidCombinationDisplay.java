package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.recipe.barrel.FluidCombinationRecipe;

import java.util.List;
import java.util.Optional;

public class FluidCombinationDisplay implements Display {
    public final BarrelMode result;
    public final EntryIngredient above;
    public final EntryIngredient inside;
    private final Identifier id;

    public FluidCombinationDisplay(FluidCombinationRecipe recipe) {
        this.inside = recipe.getContained().asReiIngredient();
        this.above = recipe.getOther().asReiIngredient();
        this.result = recipe.getResult();
        this.id = recipe.getId();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.FLUID_ABOVE;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(inside, above);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(result.getReiResult());
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return Optional.of(id);
    }
}