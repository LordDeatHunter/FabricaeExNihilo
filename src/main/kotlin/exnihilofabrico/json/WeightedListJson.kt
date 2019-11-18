package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.crafting.WeightedList
import exnihilofabrico.util.getId
import net.minecraft.block.Block
import java.lang.reflect.Type

object WeightedListJson: JsonDeserializer<WeightedList>, JsonSerializer<WeightedList> {
    val TYPE_TOKEN: Type = object: TypeToken<WeightedList>(){}.type
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): WeightedList {
        return WeightedList(
            json.asJsonObject.entrySet().map { (k, v) ->
                context.deserialize<Block>(JsonPrimitive(k), object : TypeToken<Block>() {}.type) to v.asInt
            }.toMap().toMutableMap()
        )
    }

    override fun serialize(src: WeightedList, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        ExNihiloFabrico.LOGGER.info("Serializing: ${src}")
        val json = JsonObject()
        src.values.forEach { (k,v) ->
            json.add(k.getId().toString(), JsonPrimitive(v))
        }
        return json
    }

}