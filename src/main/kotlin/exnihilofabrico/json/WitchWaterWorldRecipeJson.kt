package exnihilofabrico.json

import com.google.gson.*
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.recipes.witchwater.WitchWaterWorldRecipe
import java.lang.reflect.Type

object WitchWaterWorldRecipeJson: JsonDeserializer<WitchWaterWorldRecipe>, JsonSerializer<WitchWaterWorldRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): WitchWaterWorldRecipe {
        return WitchWaterWorldRecipe(
            FluidIngredient.fromJson(json.asJsonObject["fluid"], context),
            WeightedListJson.deserialize(json.asJsonObject["results"], WeightedListJson.TYPE_TOKEN, context)
        )
    }

    override fun serialize(src: WitchWaterWorldRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("fluid", src.fluid.toJson(context))
        obj.add("results", WeightedListJson.serialize(src.results, WeightedListJson.TYPE_TOKEN, context))
        return obj
    }

}
