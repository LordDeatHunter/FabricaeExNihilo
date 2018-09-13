package exnihilocreatio.json

import com.google.gson.*
import exnihilocreatio.registries.types.Compostable
import exnihilocreatio.texturing.Color
import exnihilocreatio.util.BlockInfo

import java.lang.reflect.Type

object CustomCompostableJson : JsonDeserializer<Compostable>, JsonSerializer<Compostable> {
    override fun serialize(src: Compostable, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.addProperty("value", src.value)
        obj.add("compostBlock", context.serialize(src.compostBlock, BlockInfo::class.java))
        if (src.color != Color.INVALID_COLOR) {
            obj.add("color", context.serialize(src.color, Color::class.java))
        }

        return obj
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Compostable {
        val helper = JsonHelper(json)
        val obj = json.asJsonObject

        val value = helper.getDouble("value").toFloat()
        var color = Color.INVALID_COLOR
        if (obj.has("color")) {
            color = context.deserialize(json.asJsonObject.get("color"), Color::class.java)
        }

        val result = context.deserialize<BlockInfo>(json.asJsonObject.get("compostBlock"), BlockInfo::class.java)

        return Compostable(value, color, result)
    }
}
