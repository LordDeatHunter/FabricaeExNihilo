package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.SpawnEggItem;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.barrels.modes.FluidMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.ItemMode;
import wraith.fabricaeexnihilo.recipe.barrel.AlchemyRecipe;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class AlchemyDisplay implements Display {

    private final EntryIngredient barrel;
    private final List<EntryIngredient> catalyst;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;
    private final List<EntryIngredient> reactant;

    public AlchemyDisplay(AlchemyRecipe recipe) {
        this.catalyst = recipe.getCatalyst().streamEntries().map(EntryIngredients::of).toList();
        this.reactant = recipe.getReactant().streamEntries().map(EntryIngredients::of).toList();

        this.inputs = new ArrayList<>();
        this.inputs.addAll(this.catalyst);
        this.inputs.addAll(this.reactant);

        this.barrel = EntryIngredients.of(ItemUtils.getExNihiloItemStack("oak_barrel"));

        this.outputs = new ArrayList<>();
        var mode = recipe.getResult();
        if (mode instanceof ItemMode itemMode) {
            outputs.add(EntryIngredients.of(itemMode.getStack()));
        } else if (mode instanceof FluidMode fluidMode) {
            var fluid = fluidMode.getFluid().getFluid();
            if (fluid != null) {
                outputs.add(EntryIngredients.of(fluid));
            } else {
                outputs.add(EntryIngredient.empty());
            }
        } else {
            outputs.add(EntryIngredient.empty());
        }
        outputs.add(EntryIngredients.of(recipe.getByproduct().stack()));
        outputs.add(!recipe.getToSpawn().isEmpty() ? EntryIngredients.of(SpawnEggItem.forEntity(recipe.getToSpawn().getType())) : EntryIngredient.empty());
    }

    public EntryIngredient getBarrel() {
        return this.barrel;
    }

    public List<EntryIngredient> getCatalyst() {
        return catalyst;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.ALCHEMY;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return this.inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return this.outputs;
    }

    public List<EntryIngredient> getReactant() {
        return this.reactant;
    }

}