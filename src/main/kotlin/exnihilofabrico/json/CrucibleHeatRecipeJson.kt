package exnihilofabrico.json

import com.google.gson.*
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.recipes.crucible.CrucibleHeatRecipe
import java.lang.reflect.Type

object CrucibleHeatRecipeJson: JsonDeserializer<CrucibleHeatRecipe>, JsonSerializer<CrucibleHeatRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): CrucibleHeatRecipe {
        val obj = json.asJsonObject
        return CrucibleHeatRecipe(
            if (obj.has("block"))
                ItemIngredient.fromJson(obj.get("block"), context)
            else
                ItemIngredient.EMPTY,
            if (obj.has("fluid"))
                FluidIngredient.fromJson(obj.get("fluid"), context)
            else
                FluidIngredient.EMPTY,
            obj["heat"].asInt
        )
    }

    override fun serialize(src: CrucibleHeatRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        if(!src.ingredient.isEmpty())
            obj.add("block", src.ingredient.toJson(context))
        if(!src.fluid.isEmpty())
            obj.add("fluid", src.fluid.toJson(context))
        obj.add("heat", context.serialize(src.value))
        return obj
    }

}