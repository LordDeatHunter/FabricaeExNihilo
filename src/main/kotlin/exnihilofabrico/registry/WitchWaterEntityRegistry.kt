package exnihilofabrico.registry

import exnihilofabrico.api.crafting.EntityIngredient
import exnihilofabrico.api.registry.IWitchWaterEntityRegistry
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.tag.Tag

data class WitchWaterEntityRegistry(val registry: MutableMap<EntityIngredient, EntityType<*>> = HashMap()): IWitchWaterEntityRegistry {
    override fun clear() = registry.clear()

    override fun register(ingredient: EntityIngredient, result: EntityType<*>) {
        registry[ingredient] = result
    }

    override fun register(input: EntityType<*>, result: EntityType<*>) {
        registry[EntityIngredient(listOf(input))] = result
    }

    override fun register(tag: Tag<EntityType<*>>, result: EntityType<*>) {
        registry[EntityIngredient(tag)] = result
    }

    override fun getResult(input: Entity) = getResult(input.type)
    override fun getResult(input: EntityType<*>): EntityType<*>? {
        registry.forEach {
            if(it.key.test(input))
                return it.value
        }
        return null
    }

}