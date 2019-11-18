package exnihilofabrico.json

import com.google.gson.*
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.recipes.crucible.CrucibleRecipe
import exnihilofabrico.json.FluidVolumeJson.TYPE_TOKEN
import java.lang.reflect.Type

object CrucibleRecipeJson: JsonDeserializer<CrucibleRecipe>, JsonSerializer<CrucibleRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): CrucibleRecipe {
        val obj = json.asJsonObject
        return CrucibleRecipe(
            ItemIngredient.fromJson(obj.get("input"), context),
            FluidVolumeJson.deserialize(obj.get("output"), TYPE_TOKEN, context)
        )
    }

    override fun serialize(src: CrucibleRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("input", src.input.toJson(context))
        obj.add("output", FluidVolumeJson.serialize(src.output, TYPE_TOKEN, context))
        return obj
    }

}