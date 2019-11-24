package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.EntityTypeIngredient
import exnihilofabrico.api.recipes.witchwater.WitchWaterEntityRecipe
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.village.VillagerProfession

interface IWitchWaterEntityRegistry: IRegistry<WitchWaterEntityRecipe> {
    fun register(target: EntityTypeIngredient, profession: VillagerProfession?, spawn: EntityType<*>) =
            register(WitchWaterEntityRecipe(target, profession, spawn))
    fun register(target: EntityTypeIngredient, spawn: EntityType<*>) =
            register(target, null, spawn)
    fun register(target: EntityType<*>, profession: VillagerProfession?, spawn: EntityType<*>) =
            register(EntityTypeIngredient(target), profession, spawn)
    fun register(target: EntityType<*>, spawn: EntityType<*>)=
        register(EntityTypeIngredient(target), null, spawn)

    fun getSpawn(entity: Entity): EntityType<*>?

    fun getAll(): List<WitchWaterEntityRecipe>

}