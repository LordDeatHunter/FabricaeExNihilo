package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.SpawnEggItem;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.barrel.AlchemyRecipe;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.List;

public class AlchemyDisplay implements Display {
    public final EntryIngredient catalyst;
    public final EntryIngredient reactant;
    public final EntryIngredient byproduct;
    public final EntryIngredient result;
    public final EntryIngredient entity;

    public AlchemyDisplay(AlchemyRecipe recipe) {
        this.catalyst = EntryIngredients.ofIngredient(recipe.getCatalyst());
        this.reactant = RegistryEntryLists.asReiIngredient(recipe.getReactant(), EntryStacks::of);
        this.result = recipe.getResult().getReiResult();
        this.byproduct = EntryIngredients.of(recipe.getByproduct().stack());
        this.entity = recipe.getToSpawn().isEmpty() ? EntryIngredient.empty() : EntryIngredients.of(SpawnEggItem.forEntity(recipe.getToSpawn().getType()));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.ALCHEMY;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(catalyst, reactant);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(result, byproduct, entity);
    }
}