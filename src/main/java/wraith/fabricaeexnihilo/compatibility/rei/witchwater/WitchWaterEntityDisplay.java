package wraith.fabricaeexnihilo.compatibility.rei.witchwater;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.SpawnEggItem;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterEntityRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record WitchWaterEntityDisplay(WitchWaterEntityRecipe recipe) implements Display {
    
    @Override
    public List<EntryIngredient> getInputEntries() {
        var list = new ArrayList<EntryIngredient>();
        list.add(EntryIngredients.of(WitchWaterFluid.BUCKET));
        list.addAll(recipe.getTarget().asREIEntries());
        return list;
    }
    
    @Override
    public List<EntryIngredient> getOutputEntries() {
        var egg = SpawnEggItem.forEntity(recipe.getResult());
        if (egg == null) {
            return new ArrayList<>();
        }
        return Collections.singletonList(EntryIngredients.of(egg));
    }
    
    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.WITCH_WATER_ENTITY;
    }
    
}
