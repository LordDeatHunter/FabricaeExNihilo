package wraith.fabricaeexnihilo.api.recipes.witchwater;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillagerProfession;
import wraith.fabricaeexnihilo.api.crafting.EntityTypeIngredient;

import java.util.function.Predicate;

public record WitchWaterEntityRecipe(EntityTypeIngredient target,
                                     VillagerProfession profession,
                                     EntityType<?> toSpawn) implements Predicate<Entity> {

    public boolean test(Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            return false;
        }
        if (target.test(entity.getType())) {
            return entity instanceof VillagerEntity villager ? villager.getVillagerData().getProfession() == profession : profession == null;
        }
        return false;
    }

}
