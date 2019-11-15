package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.EntityTypeIngredient
import java.lang.reflect.Type

object EntityTypeIngredientJson: JsonDeserializer<EntityTypeIngredient>,
    JsonSerializer<EntityTypeIngredient> {
    val TYPE_TOKEN: Type = object: TypeToken<EntityTypeIngredient>(){}.type
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        EntityTypeIngredient.fromJson(json, context)

    @Throws(JsonParseException::class)
    override fun serialize(src: EntityTypeIngredient, typeOfSrc: Type, context: JsonSerializationContext) = src.toJson(context)
}