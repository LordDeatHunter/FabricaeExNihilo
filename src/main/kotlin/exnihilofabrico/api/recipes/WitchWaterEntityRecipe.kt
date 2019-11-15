package exnihilofabrico.api.recipes

import exnihilofabrico.api.crafting.EntityTypeIngredient
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.village.VillagerProfession
import java.util.function.Predicate

data class WitchWaterEntityRecipe(val target: EntityTypeIngredient,
                                  val profession: VillagerProfession?,
                                  val tospawn: EntityType<*>
): Predicate<Entity?> {
    override fun test(entity: Entity?): Boolean {
        if(entity == null || entity !is LivingEntity)
            return false
        if(target.test(entity.type)) {
            if(entity is VillagerEntity) {
                return entity.villagerData.profession == profession
            }
            else
                return profession == null

        }
        return false
    }
}