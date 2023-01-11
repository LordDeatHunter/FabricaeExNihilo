package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.recipe.barrel.FluidTransformationRecipe;

import java.util.List;
import java.util.Optional;

public class TransformingDisplay implements Display {

    public final EntryIngredient catalyst;
    public final EntryIngredient fluid;
    public final BarrelMode result;
    private final Identifier id;

    public TransformingDisplay(FluidTransformationRecipe recipe) {
        this.catalyst = recipe.getCatalyst().asReiIngredient();
        this.fluid = recipe.getFluid().asReiIngredient();
        this.result = recipe.getResult();
        this.id = recipe.getId();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.TRANSFORMING;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(catalyst, fluid);
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