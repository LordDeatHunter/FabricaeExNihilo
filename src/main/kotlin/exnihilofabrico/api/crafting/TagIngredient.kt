package exnihilofabrico.api.crafting

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.json.*
import exnihilofabrico.util.getFluid
import exnihilofabrico.util.getId
import net.minecraft.block.Block
import net.minecraft.block.FluidBlock
import net.minecraft.entity.EntityType
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.tag.Tag
import java.util.function.Predicate

/**
 * An ingredient class that remembers its tag names
 */
data class TagIngredient<T>(val tags: MutableSet<Tag<T>> = mutableSetOf(), val matches: MutableSet<T> = mutableSetOf()): Predicate<T> {
    constructor(vararg tags: Tag<T>): this(tags.toMutableSet())
    constructor(vararg matches: T): this(matches = matches.toMutableSet())

    private val tagType = object: TypeToken<Tag<T>>(){}.type
    private val matchType = object: TypeToken<T>(){}.type

    override fun test(t: T): Boolean {
        return tags.any { it.contains(t) } || matches.contains(t)
    }

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
                is Item -> it.getId()
                is Block -> it.getId()
                is Fluid -> it.getId()
                is EntityType<*> -> it.getId()
                else -> throw Exception("Failed to serialize entry: ${it}")
            }.toString()
            json.add(JsonPrimitive(entry))
        }

        return if(json.size() == 1) json.first() else json
    }

    companion object {

        fun <T> fromTags(vararg tags: Tag<T>) = TagIngredient<T>(tags.toMutableSet())
        fun <T> fromMatches(vararg matches: T) = TagIngredient<T>(mutableSetOf(), matches.toMutableSet())

        inline fun <reified T> fromJson(json: JsonElement, context: JsonDeserializationContext): TagIngredient<T> {
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
                            else -> throw JsonParseException("Failed to deserialize tag: ${it}")
                        }
                        if(tag != null)
                            tags.add(tag as Tag<T>)
                    }
                    else {
                        matches.add(
                            when(T::class.java) {
                                Item::class.java -> ItemJson.deserialize(it, matchType, context) as T
                                Fluid::class.java -> FluidJson.deserialize(it, matchType, context) as T
                                Block::class.java -> BlockJson.deserialize(it, matchType, context) as T
                                EntityType::class.java -> EntityTypeJson.deserialize(it, matchType, context) as T
                                else -> throw JsonParseException("Unable to deserialize TagIngredient: ${it}")
                            }
                        )
                    }
                }

            return TagIngredient(tags, matches)
        }

        fun of(vararg stacks: ItemStack): TagIngredient<Item> = TagIngredient(matches = stacks.map { it.item }.toMutableSet())
        fun of(vararg items: Item): TagIngredient<Item> = TagIngredient(matches = items.toMutableSet())
        fun of(vararg items: ItemConvertible): TagIngredient<Item> = TagIngredient(matches = items.map { it.asItem() }.toMutableSet())
        fun of(vararg fluids: Fluid): TagIngredient<Fluid> = TagIngredient(matches = fluids.toMutableSet())
        fun of(vararg entities: EntityType<*>): TagIngredient<EntityType<*>> = TagIngredient(matches = entities.toMutableSet())
    }
}

fun TagIngredient<Item>.test(stack: ItemStack) = !stack.isEmpty && test(stack.item)
fun TagIngredient<Item>.test(item: ItemConvertible) = test(item.asItem())

fun TagIngredient<Fluid>.test(stack: FluidStack) = !stack.isEmpty() && test(stack.asFluid())
fun TagIngredient<Fluid>.test(block: FluidBlock) = test(block.getFluid())
fun TagIngredient<Fluid>.test(state: FluidState) = test(state.fluid)