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

    private final List<EntryIngredient> input;
    private final List<EntryIngredient> output;
    private final VillagerProfession profession;
    private final EntityTypeIngredient target;

    public WitchWaterEntityDisplay(WitchWaterEntityRecipe recipe) {
        this.input = recipe.getTarget().flattenListOfEggStacks(EntryIngredients::of);
        var result = recipe.getResult();
        var spawnEgg = SpawnEggItem.forEntity(result);
        this.output = new ArrayList<>();
        this.output.add(spawnEgg != null ? EntryIngredients.of(spawnEgg) : EntryIngredient.empty());
        this.profession = recipe.getProfession();
        this.target = recipe.getTarget();
    }

    public EntityTypeIngredient getTarget() {
        return target;
    }

    public VillagerProfession getProfession() {
        return profession;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return input;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return output;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.WITCH_WATER_ENTITY;
    }

}
