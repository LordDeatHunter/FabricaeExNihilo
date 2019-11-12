package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.recipes.WitchWaterWorldRecipe
import exnihilofabrico.api.registry.WeightedList
import java.lang.reflect.Type

object WitchWaterWorldRecipeJson: JsonDeserializer<WitchWaterWorldRecipe>, JsonSerializer<WitchWaterWorldRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): WitchWaterWorldRecipe {
        return WitchWaterWorldRecipe(
            FluidIngredient.fromJson(json.asJsonObject["fluid"], context),
            context.deserialize(json.asJsonObject["results"], object: TypeToken<WeightedList>(){}.type)
        )
    }

    override fun serialize(src: WitchWaterWorldRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("fluid", src.fluid.toJson(context))
        obj.add("results", context.serialize(src.results))
        return obj
    }

}
