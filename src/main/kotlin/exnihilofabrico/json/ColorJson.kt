package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.util.Color
import java.lang.reflect.Type

object ColorJson: JsonDeserializer<Color>, JsonSerializer<Color> {
    val TYPE_TOKEN: Type = object: TypeToken<Color>(){}.type
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) = Color(json.asString)

    @Throws(JsonParseException::class)
    override fun serialize(src: Color, typeOfSrc: Type, context: JsonSerializationContext) = JsonPrimitive(src.toHex())
}