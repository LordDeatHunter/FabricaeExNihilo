package exnihilocreatio.json

import com.google.gson.*
import exnihilocreatio.util.EntityInfo

import java.lang.reflect.Type

object CustomEntityInfoJson : JsonDeserializer<EntityInfo>, JsonSerializer<EntityInfo> {
    override fun serialize(src: EntityInfo, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
            JsonPrimitive(src.name)

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): EntityInfo =
            EntityInfo(json.asString)

}
