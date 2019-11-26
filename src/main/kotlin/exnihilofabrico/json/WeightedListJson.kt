package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.WeightedList
import exnihilofabrico.util.getId
import java.lang.reflect.Type

object WeightedListJson: JsonDeserializer<WeightedList>, JsonSerializer<WeightedList> {
    val TYPE_TOKEN: Type = object: TypeToken<WeightedList>(){}.type
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): WeightedList {
        return WeightedList(
            json.asJsonObject.entrySet().map { (k, v) ->
                BlockJson.deserialize(JsonPrimitive(k), BLOCK_TYPE_TOKEN, context) to v.asInt
            }.toMap().toMutableMap()
        )
    }

    override fun serialize(src: WeightedList, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = JsonObject()
        src.values.forEach { (k,v) ->
            json.add(k.getId().toString(), JsonPrimitive(v))
        }
        return json
    }

}