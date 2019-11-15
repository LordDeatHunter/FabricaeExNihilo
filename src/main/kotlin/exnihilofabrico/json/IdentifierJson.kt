package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import net.minecraft.util.Identifier
import java.lang.reflect.Type

object IdentifierJson: JsonDeserializer<Identifier>, JsonSerializer<Identifier> {
    val TYPE_TOKEN: Type? = object: TypeToken<Identifier>(){}.type
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) = Identifier(json.asJsonPrimitive.asString)

    @Throws(JsonParseException::class)
    override fun serialize(src: Identifier, typeOfSrc: Type, context: JsonSerializationContext?) = JsonPrimitive(src.toString())

}