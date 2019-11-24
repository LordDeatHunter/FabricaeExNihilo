package exnihilofabrico.api.crafting

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import exnihilofabrico.util.getId
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class EntityTypeIngredient(tags: MutableCollection<Tag<EntityType<*>>> = mutableListOf(), matches: MutableSet<EntityType<*>> = mutableSetOf()):
    AbstractIngredient<EntityType<*>>(tags, matches) {

    constructor(vararg matches: EntityType<*>): this(mutableListOf(), matches.toMutableSet())
    constructor(vararg tags: Tag<EntityType<*>>): this(tags.toMutableList(), mutableSetOf())

    fun test(entity: Entity) = test(entity.type)

    override fun serializeElement(t: EntityType<*>, context: JsonSerializationContext) =
        JsonPrimitive(t.getId().toString())

    override fun equals(other: Any?): Boolean {
        return (other as? EntityTypeIngredient)?.let { other ->
            this.tags.size == other.tags.size &&
                    this.matches.size == other.matches.size &&
                    this.tags.containsAll(other.tags) &&
                    this.matches.containsAll(other.matches)
        }?: false
    }

    override fun hashCode(): Int {
        return tags.hashCode() xor matches.hashCode()
    }
    companion object {
        val EMPTY = EntityTypeIngredient()

        fun fromJson(json: JsonElement, context: JsonDeserializationContext): EntityTypeIngredient =
            fromJson(json,
                context,
                { deserializeTag(it, context) },
                { deserializeMatch(it, context) },
                { tags: MutableCollection<Tag<EntityType<*>>>, matches: MutableSet<EntityType<*>> ->
                    EntityTypeIngredient(
                        tags,
                        matches
                    )
                })

        fun deserializeTag(json: JsonElement, context: JsonDeserializationContext): Tag<EntityType<*>> =
            TagRegistry.entityType(Identifier(json.asString.split("#").last()))
        fun deserializeMatch(json: JsonElement, context: JsonDeserializationContext) =
            Registry.ENTITY_TYPE[(Identifier(json.asString))]
    }
}