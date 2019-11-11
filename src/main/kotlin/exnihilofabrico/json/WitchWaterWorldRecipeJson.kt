package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.TagIngredient
import exnihilofabrico.api.recipes.WitchWaterWorldRecipe
import exnihilofabrico.api.registry.WeightedList
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import java.lang.reflect.Type

object WitchWaterWorldRecipeJson: JsonDeserializer<WitchWaterWorldRecipe>, JsonSerializer<WitchWaterWorldRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): WitchWaterWorldRecipe {
        return WitchWaterWorldRecipe(
            TagIngredient.fromJson(json.asJsonObject["fluid"], context),
            context.deserialize(json.asJsonObject["results"], object: TypeToken<WeightedList>(){}.type)
        )
    }

    override fun serialize(src: WitchWaterWorldRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("fluid", src.fluid.toJson<Fluid>(context))
        obj.add("results", context.serialize(src.results))
        return obj
    }

}
