package wraith.fabricaeexnihilo.compatibility.rei.witchwater;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.village.VillagerProfession;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.util.EntityTypeIngredient;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterEntityRecipe;

import java.util.ArrayList;
import java.util.List;

public class WitchWaterEntityDisplay implements Display {

    private final List<EntryIngredient> inputs = new ArrayList<>();
    private final List<EntryIngredient> outputs = new ArrayList<>();
    private final VillagerProfession profession;
    private final EntityType<?> result;
    private final List<EntityType<?>> target;

    public WitchWaterEntityDisplay(WitchWaterEntityRecipe recipe) {
        this.target = recipe.getTarget().flattenListOfEntities();
        this.result = recipe.getResult();
        this.profession = recipe.getProfession();
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

    public EntityType<?> getResult() {
        return result;
    }

    public List<EntityType<?>> getTarget() {
        return target;
    }

    public VillagerProfession getProfession() {
        return profession;
    }

}
