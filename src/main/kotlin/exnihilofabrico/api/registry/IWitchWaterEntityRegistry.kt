package exnihilofabrico.api.registry

import exnihilofabrico.api.crafting.EntityIngredient
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.tag.Tag

interface IWitchWaterEntityRegistry {
    fun clear()
    fun register(ingredient: EntityIngredient, result: EntityType<*>)
    fun register(input: EntityType<*>, result: EntityType<*>)
    fun register(tag: Tag<EntityType<*>>, result: EntityType<*>)
    fun getResult(input: Entity): EntityType<*>?
    fun getResult(input: EntityType<*>): EntityType<*>?
}