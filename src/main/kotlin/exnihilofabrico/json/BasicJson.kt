package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.util.getID
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.lang.reflect.Type

object ItemJson: JsonDeserializer<Item>, JsonSerializer<Item> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        Registry.ITEM[context.deserialize<Identifier>(json, IdentifierJson.TYPE_TOKEN)]

    override fun serialize(src: Item, typeOfSrc: Type, context: JsonSerializationContext) =
            context.serialize(src.getID())
}

object BlockJson: JsonDeserializer<Block>, JsonSerializer<Block> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        Registry.BLOCK[context.deserialize<Identifier>(json, IdentifierJson.TYPE_TOKEN)]

    override fun serialize(src: Block, typeOfSrc: Type, context: JsonSerializationContext) =
        context.serialize(src.getID())

}

object FluidJson: JsonDeserializer<Fluid>, JsonSerializer<Fluid> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        Registry.FLUID[context.deserialize<Identifier>(json, IdentifierJson.TYPE_TOKEN)]

    override fun serialize(src: Fluid, typeOfSrc: Type, context: JsonSerializationContext) =
        context.serialize(src.getID())
}

object EntityTypeJson: JsonDeserializer<EntityType<*>>, JsonSerializer<EntityType<*>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        Registry.ENTITY_TYPE[context.deserialize<Identifier>(json, IdentifierJson.TYPE_TOKEN)]

    override fun serialize(src: EntityType<*>, typeOfSrc: Type, context: JsonSerializationContext) =
        context.serialize(src.getID())
}

object ItemTagJson: JsonDeserializer<Tag<Item>>, JsonSerializer<Tag<Item>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        TagRegistry.item(getTagID(json.asString))

    override fun serialize(src: Tag<Item>, typeOfSrc: Type, context: JsonSerializationContext) =
        JsonPrimitive("item#" + context.serialize(src.id).asString)

}

object BlockTagJson: JsonDeserializer<Tag<Block>>, JsonSerializer<Tag<Block>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        TagRegistry.block(getTagID(json.asString))

    override fun serialize(src: Tag<Block>, typeOfSrc: Type, context: JsonSerializationContext) =
        JsonPrimitive("block#" + context.serialize(src.id).asString)

}

object FluidTagJson: JsonDeserializer<Tag<Fluid>>, JsonSerializer<Tag<Fluid>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        TagRegistry.fluid(getTagID(json.asString))

    override fun serialize(src: Tag<Fluid>, typeOfSrc: Type, context: JsonSerializationContext) =
        JsonPrimitive("fluid#" + context.serialize(src.id).asString)

}

object EntityTypeTagJson: JsonDeserializer<Tag<EntityType<*>>>, JsonSerializer<Tag<EntityType<*>>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        TagRegistry.entityType(getTagID(json.asString))

    override fun serialize(src: Tag<EntityType<*>>, typeOfSrc: Type, context: JsonSerializationContext) =
        JsonPrimitive("entity#" + context.serialize(src.id).asString)

}

fun getTagID(string: String) = Identifier(string.split("#").last())

val ITEM_TAG_TYPE_TOKEN = object: TypeToken<Tag<Item>>(){}.type
val BLOCK_TAG_TYPE_TOKEN = object: TypeToken<Tag<Block>>(){}.type
val FLUID_TAG_TYPE_TOKEN = object: TypeToken<Tag<Fluid>>(){}.type
val ENTITY_TAG_TYPE_TOKEN = object: TypeToken<Tag<EntityType<*>>>(){}.type

val ITEM_TYPE_TOKEN = object: TypeToken<Item>(){}.type
val BLOCK_TYPE_TOKEN = object: TypeToken<Block>(){}.type
val FLUID_TYPE_TOKEN = object: TypeToken<Fluid>(){}.type
val ENTITY_TYPE_TOKEN = object: TypeToken<EntityType<*>>(){}.type