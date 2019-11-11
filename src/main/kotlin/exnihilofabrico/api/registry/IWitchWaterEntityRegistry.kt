package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.EntityStack
import exnihilofabrico.api.crafting.TagIngredient
import exnihilofabrico.api.recipes.WitchWaterEntityRecipe
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.village.VillagerProfession

interface IWitchWaterEntityRegistry{
    fun register(target: TagIngredient<EntityType<*>>, profession: VillagerProfession?, spawn: EntityType<*>): Boolean
    fun register(target: TagIngredient<EntityType<*>>, spawn: EntityType<*>) =
            register(target, null, spawn)
    fun register(target: EntityType<*>, profession: VillagerProfession?, spawn: EntityType<*>) =
            register(TagIngredient.fromMatches(target), profession, spawn)
    fun register(target: EntityType<*>, spawn: EntityType<*>)=
        register(TagIngredient.fromMatches(target), null, spawn)

    fun getSpawn(entity: Entity): EntityType<*>?

    fun clear()
    fun getAll(): List<WitchWaterEntityRecipe>

}