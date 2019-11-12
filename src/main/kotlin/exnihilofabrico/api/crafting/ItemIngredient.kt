package exnihilofabrico.api.crafting

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import exnihilofabrico.util.getId
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class ItemIngredient(tags: MutableCollection<Tag<Item>> = mutableListOf(), matches: MutableSet<Item> = mutableSetOf()):
    AbstractIngredient<Item>(tags, matches) {

    constructor(vararg matches: Item): this(mutableListOf(), matches.toMutableSet())
    constructor(vararg matches: ItemStack): this(mutableListOf(), matches.map { it.item }.toMutableSet())
    constructor(vararg matches: ItemConvertible): this(mutableListOf(), matches.map { it.asItem() }.toMutableSet())
    constructor(vararg tags: Tag<Item>): this(tags.toMutableList(), mutableSetOf())

    override fun serializeElement(t: Item, context: JsonSerializationContext) =
        JsonPrimitive(t.getId().toString())

    fun test(stack: ItemStack) = test(stack.item)
    fun test(item: ItemConvertible) = test(item.asItem())

    companion object {
        val EMPTY = ItemIngredient()

        fun fromJson(json: JsonElement, context: JsonDeserializationContext) =
            fromJson(json,
                context,
                { deserializeTag(it, context) },
                { deserializeMatch(it, context) },
                { tags: MutableCollection<Tag<Item>>, matches: MutableSet<Item> ->
                    ItemIngredient(
                        tags,
                        matches
                    )
                })

        fun deserializeTag(json: JsonElement, context: JsonDeserializationContext) =
            TagRegistry.item(Identifier(json.asString.split("#").last()))
        fun deserializeMatch(json: JsonElement, context: JsonDeserializationContext) =
            Registry.ITEM[(Identifier(json.asString))]

    }
}