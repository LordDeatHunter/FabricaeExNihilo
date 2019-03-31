package exnihilocreatio.json

import com.google.gson.*
import exnihilocreatio.texturing.Color
import exnihilocreatio.util.LogUtil

import java.lang.reflect.Type

object CustomColorJson : JsonDeserializer<Color>, JsonSerializer<Color> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Color {
        if (json.isJsonPrimitive) {
            val prim = json.asJsonPrimitive
            when {
                prim.isString -> return Color(prim.asString)
                prim.isNumber -> return Color(prim.asInt)
                else -> LogUtil.warn("Invalid Color primitive for $json")
            }
        } else {
            val helper = JsonHelper(json)
            return Color(helper.getDouble("r").toFloat(), helper.getDouble("g").toFloat(), helper.getDouble("b").toFloat(), helper.getDouble("a").toFloat())
        }

        return Color.INVALID_COLOR
    }

    override fun serialize(src: Color, typeOfSrc: Type, context: JsonSerializationContext) =
            JsonPrimitive(src.asHexNoAlpha)

}
