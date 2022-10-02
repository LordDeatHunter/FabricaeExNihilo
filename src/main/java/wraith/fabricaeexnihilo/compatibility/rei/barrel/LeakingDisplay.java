package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.barrel.LeakingRecipe;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.List;
import java.util.Optional;

public class LeakingDisplay implements Display {
    public final long amount;
    public final EntryIngredient block;
    public final EntryIngredient fluid;
    public final EntryIngredient output;
    private final Identifier id;

    public LeakingDisplay(LeakingRecipe recipe) {
        this.amount = recipe.getAmount();
        this.block = RegistryEntryLists.asReiIngredient(recipe.getBlock());
        this.fluid = RegistryEntryLists.asReiIngredient(recipe.getFluid(), fluid -> EntryStacks.of(fluid, amount));
        this.output = EntryIngredients.of(recipe.getResult());
        this.id = recipe.getId();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.LEAKING;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(block, fluid);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(this.output);
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return Optional.of(id);
    }
}
