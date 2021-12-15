package wraith.fabricaeexnihilo.api.registry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.village.VillagerProfession;
import wraith.fabricaeexnihilo.api.crafting.EntityTypeIngredient;
import wraith.fabricaeexnihilo.api.recipes.witchwater.WitchWaterEntityRecipe;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public interface WitchWaterEntityRegistry extends Registry<WitchWaterEntityRecipe> {

    default boolean register(EntityTypeIngredient target, @Nullable VillagerProfession profession, EntityType<?> spawn) {
        return register(new WitchWaterEntityRecipe(target, profession, spawn));
    }

    default boolean register(EntityTypeIngredient target, EntityType<?> spawn) {
        return register(target, null, spawn);
    }

    default boolean register(EntityType<?> target, @Nullable VillagerProfession profession, EntityType<?> spawn) {
        return register(new EntityTypeIngredient(target), profession, spawn);
    }

    default boolean register(EntityType<?> target, EntityType<?> spawn) {
        return register(new EntityTypeIngredient(target), null, spawn);
    }

    @Nullable
    EntityType<?> getSpawn(Entity entity);

    List<WitchWaterEntityRecipe> getAll();

    // All recipes, chunked/broken up for pagination
    Collection<WitchWaterEntityRecipe> getREIRecipes();

}