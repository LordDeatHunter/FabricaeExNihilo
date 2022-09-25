package wraith.fabricaeexnihilo.compatibility.rei.witchwater;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.village.VillagerProfession;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterEntityRecipe;

import java.util.List;
import java.util.stream.Collectors;

public class WitchWaterEntityDisplay implements Display {
    public final VillagerProfession profession;
    public final EntityType<?> result;
    public final List<EntityType<?>> target;

    public WitchWaterEntityDisplay(WitchWaterEntityRecipe recipe) {
        this.target = recipe.getTarget()
                .stream()
                .map(RegistryEntry::value)
                // Use collector to avoid generics error
                .collect(Collectors.toList());
        this.result = recipe.getResult();
        this.profession = recipe.getProfession();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.WITCH_WATER_ENTITY;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of();
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of();
    }
}
