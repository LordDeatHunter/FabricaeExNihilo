package exnihilofabrico.json

import com.google.gson.*
import net.minecraft.recipe.Ingredient
import java.lang.reflect.Type

object IngredientJson: JsonDeserializer<Ingredient>, JsonSerializer<Ingredient> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?) = Ingredient.fromJson(json)

    @Throws(JsonParseException::class)
    override fun serialize(src: Ingredient, typeOfSrc: Type?, context: JsonSerializationContext?) = src.toJson()

}