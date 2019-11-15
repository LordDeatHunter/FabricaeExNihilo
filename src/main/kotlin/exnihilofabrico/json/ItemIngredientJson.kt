package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.ItemIngredient
import java.lang.reflect.Type

object ItemIngredientJson: JsonDeserializer<ItemIngredient>, JsonSerializer<ItemIngredient> {
    val TYPE_TOKEN: Type? = object: TypeToken<ItemIngredient>(){}.type
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) = ItemIngredient.fromJson(json, context)

    @Throws(JsonParseException::class)
    override fun serialize(src: ItemIngredient, typeOfSrc: Type, context: JsonSerializationContext) = src.toJson(context)
}

