package exnihilocreatio.json

import com.google.gson.*
import exnihilocreatio.registries.types.WitchWaterWorld
import exnihilocreatio.util.BlockInfo
import java.lang.reflect.Type

object CustomWitchWaterWorld : JsonDeserializer<WitchWaterWorld>, JsonSerializer<WitchWaterWorld> {
    override fun serialize(src: WitchWaterWorld, typeOfSrc: Type, context: JsonSerializationContext) : JsonObject {
        val json = JsonObject()
        for(entry in src.toMap())
            json.add(entry.key.toString(), JsonPrimitive(entry.value))
        return json
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): WitchWaterWorld {
        val www = WitchWaterWorld(ArrayList(), ArrayList())
        val entries = json.asJsonObject.entrySet()
        for(entry in entries)
            www.add(BlockInfo(entry.key),entry.value.asInt)
        return www
    }
}