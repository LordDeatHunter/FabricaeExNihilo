package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.barrel.AlchemyRecipe;

import java.util.List;
import java.util.Optional;

public class AlchemyDisplay implements Display {
    public final EntryIngredient catalyst;
    public final EntryIngredient reactant;
    public final EntryIngredient byproduct;
    public final EntryIngredient result;
    public final EntryIngredient entity;
    private final Identifier id;

    public AlchemyDisplay(AlchemyRecipe recipe) {
        this.catalyst = EntryIngredients.ofIngredient(recipe.getCatalyst());
        this.reactant = recipe.getReactant().asReiIngredient();
        this.result = recipe.getResult().getReiResult();
        this.byproduct = EntryIngredients.of(recipe.getByproduct().stack());
        this.entity = recipe.getToSpawn().isEmpty() ? EntryIngredient.empty() : EntryIngredients.of(SpawnEggItem.forEntity(recipe.getToSpawn().getType()));
        this.id = recipe.getId();
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

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return Optional.of(id);
    }
}