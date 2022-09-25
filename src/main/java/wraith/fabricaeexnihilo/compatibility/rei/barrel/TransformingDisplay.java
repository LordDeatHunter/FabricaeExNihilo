package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.util.registry.RegistryEntry;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.recipe.barrel.FluidTransformationRecipe;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.List;

public class TransformingDisplay implements Display {
    public final EntryIngredient catalyst;
    public final EntryIngredient fluid;
    public final BarrelMode result;

    public TransformingDisplay(FluidTransformationRecipe recipe) {
        this.catalyst = RegistryEntryLists.asReiIngredient(recipe.getCatalyst());
        this.fluid = EntryIngredient.of(recipe.getFluid().stream().map(RegistryEntry::value).map(EntryStacks::of).toList());
        this.result = recipe.getResult();
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

}