package wraith.fabricaeexnihilo.compatibility.rei.witchwater;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.village.VillagerProfession;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.util.EntityTypeIngredient;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterEntityRecipe;

import java.util.ArrayList;
import java.util.List;

public class WitchWaterEntityDisplay implements Display {

    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;
    private final VillagerProfession profession;
    private final EntityTypeIngredient target;

    public WitchWaterEntityDisplay(WitchWaterEntityRecipe recipe) {
        this.inputs = recipe.getTarget().flattenListOfEggStacks(EntryIngredients::of);
        var result = recipe.getResult();
        var spawnEgg = SpawnEggItem.forEntity(result);
        this.outputs = new ArrayList<>();
        this.outputs.add(spawnEgg != null ? EntryIngredients.of(spawnEgg) : EntryIngredient.empty());
        this.profession = recipe.getProfession();
        this.target = recipe.getTarget();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.WITCH_WATER_ENTITY;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return outputs;
    }

    public VillagerProfession getProfession() {
        return profession;
    }

    public EntityTypeIngredient getTarget() {
        return target;
    }

}
