package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.FluidIngredient
import java.lang.reflect.Type

object FluidIngredientJson: JsonDeserializer<FluidIngredient>, JsonSerializer<FluidIngredient> {
    val TYPE_TOKEN = object: TypeToken<FluidIngredient>(){}.type
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        FluidIngredient.fromJson(json, context)

    @Throws(JsonParseException::class)
    override fun serialize(src: FluidIngredient, typeOfSrc: Type, context: JsonSerializationContext) = src.toJson(context)
}