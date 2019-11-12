package exnihilofabrico.api.crafting

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import net.minecraft.tag.Tag
import java.util.function.Predicate


abstract class AbstractIngredient<T>(val tags: MutableCollection<Tag<T>>, val matches: MutableSet<T>): Predicate<T> {

    constructor(vararg matches: T): this(mutableListOf(), matches.toMutableSet())

    override fun test(t: T) = tags.any { it.contains(t) } || matches.contains(t)

    abstract fun serializeElement(t: T, context: JsonSerializationContext): JsonElement

    fun toJson(context: JsonSerializationContext): JsonElement {
        val arr = JsonArray()
        tags.forEach { arr.add("#" + it.id.toString()) }
        matches.forEach { arr.add(serializeElement(it, context)) }
        return if(arr.size() > 1) arr else arr.first()
    }

    fun isEmpty() = tags.isEmpty() && matches.isEmpty()

    companion object {
        inline fun <T,U> fromJson(json: JsonElement,
                                context: JsonDeserializationContext,
                                deserializeTag: (JsonElement) -> Tag<T>,
                                deserializeMatch: (JsonElement) -> T,
                                factory: (MutableCollection<Tag<T>>, MutableSet<T>) -> U): U {
            val tags = mutableListOf<Tag<T>>()
            val matches = mutableSetOf<T>()

            (if(json.isJsonArray) json.asJsonArray else listOf(json))
                .filter { it.isJsonPrimitive }
                .forEach {
                    if(it.asString.startsWith("#"))
                        tags.add(deserializeTag(it))
                    else
                        matches.add(deserializeMatch(it))
                }
            return factory(tags, matches)
        }
    }

}

