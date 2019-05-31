package exnihilofabrico.api.crafting

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.tag.Tag

class EntityIngredient(val matches: Collection<EntityType<*>>, val tag: Tag<EntityType<*>>?) {
    constructor(tag: Tag<EntityType<*>>): this(tag.values(), tag)
    constructor(matches: Collection<EntityType<*>>): this(matches, null)

    fun test(entity: Entity) = test(entity.type)
    fun test(entity: EntityType<*>) = matches.any { it == entity }

    override fun toString(): String {
        if(tag != null) return tag.id.toString()
        return matches.toString()
    }
}