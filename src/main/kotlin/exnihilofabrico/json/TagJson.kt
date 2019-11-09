package exnihilofabrico.json

import com.google.gson.*
import exnihilofabrico.ExNihiloFabrico
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.entity.EntityType
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import java.lang.reflect.Type

object TagJson: JsonDeserializer<Tag<*>>, JsonSerializer<Tag<*>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Tag<*> {
        val splits = json.asString.split("#")
        val id = Identifier(splits[1])
        return when(splits[0]) {
            "item" -> TagRegistry.item(id)
            "fluid" -> TagRegistry.fluid(id)
            "entity" -> TagRegistry.entityType(id)
            else -> throw JsonParseException("Could not deserialize: ${json.asString} as a tag")
        }

    }

    override fun serialize(src: Tag<*>, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        ExNihiloFabrico.LOGGER.info("Serializing: src.id")
        val prefix = when(src.values().first()) {
            is Item -> "item"
            is Fluid -> "fluid"
            is EntityType<*> -> "entity"
            else -> ""
        }
        return JsonPrimitive("${prefix}#src.id")
    }

}