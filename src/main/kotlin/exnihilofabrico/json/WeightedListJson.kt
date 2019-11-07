package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.registry.WeightedList
import net.minecraft.block.Block
import java.lang.reflect.Type

class WeightedListJson: JsonDeserializer<WeightedList>, JsonSerializer<WeightedList> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): WeightedList {
        return WeightedList(
            json.asJsonObject.entrySet().map { (k,v) ->
                context.deserialize<Block>(JsonPrimitive(k), object: TypeToken<Block>(){}.type) to v.asInt }.toMap().toMutableMap()
        )
    }

    override fun serialize(src: WeightedList, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = JsonObject()
        src.values.forEach { (k,v) ->
            json.add(context.serialize(k).asString, JsonPrimitive(v))
        }
        return json
    }

}