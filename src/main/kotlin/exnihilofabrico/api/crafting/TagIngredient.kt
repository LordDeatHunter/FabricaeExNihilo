package exnihilofabrico.api.crafting

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.json.*
import exnihilofabrico.modules.fluid.FluidInstance
import exnihilofabrico.util.getID
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import java.util.function.Predicate

/**
 * An ingredient class that remembers its tag names
 */
data class TagIngredient<T>(val tags: MutableSet<Tag<T>> = mutableSetOf(), val matches: MutableSet<T> = mutableSetOf()): Predicate<T> {
    private val tagType = object: TypeToken<Tag<T>>(){}.type
    private val matchType = object: TypeToken<T>(){}.type


    override fun test(t: T) = tags.any { it.contains(t) } || matches.any { it == t }

    inline fun <reified U> toJson(context: JsonSerializationContext): JsonElement {
        val json = JsonArray()

        tags.forEach {
            val prefix = when(U::class) {
                Item::class -> "item"
                Fluid::class -> "fluid"
                Block::class -> "block"
                EntityType::class -> "entity"
                else -> throw Exception("Failed to serialize tag: ${it.id}")
            }
            json.add("${prefix}#${it.id}")
        }
        matches.forEach {
            val entry = when(it) {
                is Item -> it.getID()
                is Fluid -> it.getID()
                is EntityType<*> -> it.getID()
                else -> Identifier("air")
            }.toString()
            json.add(JsonPrimitive(entry))
        }

        return if(json.size() == 1) json.first() else json
    }

    companion object {
        val EMPTY_ITEM = TagIngredient<Item>()

        fun <T> fromTags(vararg tags: Tag<T>) = TagIngredient<T>(tags.toMutableSet())
        fun <T> fromMatches(vararg matches: T) = TagIngredient<T>(mutableSetOf(), matches.toMutableSet())

        fun <T> fromJson(json: JsonElement, context: JsonDeserializationContext): TagIngredient<T> {
            val tagType = object: TypeToken<Tag<T>>(){}.type
            val matchType = object: TypeToken<T>(){}.type

            val tags = mutableSetOf<Tag<T>>()
            val matches = mutableSetOf<T>()

            (if(json.isJsonPrimitive) listOf(json) else json.asJsonArray)
                .filter { it.isJsonPrimitive }
                .forEach {
                    if(it.asString.contains("#")) {
                        val splits = it.asString.split("#")
                        val tag = when(splits.first()) {
                            "item" -> ItemTagJson.deserialize(json, ITEM_TAG_TYPE_TOKEN, context)
                            "block" -> BlockTagJson.deserialize(json, BLOCK_TAG_TYPE_TOKEN, context)
                            "fluid" -> FluidTagJson.deserialize(json, FLUID_TAG_TYPE_TOKEN, context)
                            "entity" -> EntityTypeTagJson.deserialize(json, ENTITY_TAG_TYPE_TOKEN, context)
                            else -> throw JsonParseException("Failed to deserialize tag: ${it.asString}")
                        }
                        if(tag != null)
                            tags.add(tag as Tag<T>)
                    }
                    else
                        matches.add(context.deserialize(json, matchType))
                }

            return TagIngredient(tags, matches)
        }
    }
}

fun TagIngredient<Item>.test(stack: ItemStack) = !stack.isEmpty && test(stack.item)
fun TagIngredient<Item>.test(item: ItemConvertible) = test(item.asItem())

fun TagIngredient<Fluid>.test(stack: FluidInstance) = !stack.isEmpty() && test(stack.asFluid())

fun TagIngredient<EntityType<*>>.test(stack: EntityStack) = !stack.isEmpty() && test(stack.type)
