package exnihilofabrico.json

import com.google.gson.*
import exnihilofabrico.api.crafting.FluidIngredient
import java.lang.reflect.Type

object FluidIngredientJson: JsonSerializer<FluidIngredient>, JsonDeserializer<FluidIngredient> {
    @Throws(JsonParseException::class)
    override fun serialize(src: FluidIngredient, typeOfSrc: Type, context: JsonSerializationContext) = src.toJson()

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        FluidIngredient.fromJson(json)

}