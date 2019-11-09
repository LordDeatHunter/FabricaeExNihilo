package exnihilofabrico.json

import com.google.gson.*
import exnihilofabrico.api.crafting.TagIngredient
import exnihilofabrico.api.recipes.CrucibleHeatRecipe
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import java.lang.reflect.Type

object CrucibleHeatRecipeJson: JsonDeserializer<CrucibleHeatRecipe>, JsonSerializer<CrucibleHeatRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): CrucibleHeatRecipe {
        return CrucibleHeatRecipe(
            TagIngredient.fromJson(json.asJsonObject.get("block"), context),
            TagIngredient.fromJson(json.asJsonObject.get("fluid"), context),
            json.asJsonObject["heat"].asInt
        )
    }

    override fun serialize(src: CrucibleHeatRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        if(src.ingredient != null)
            obj.add("block", src.ingredient.toJson<Item>(context))
        if(src.fluid != null)
            obj.add("fluid", src.fluid.toJson<Fluid>(context))
        obj.add("heat", context.serialize(src.value))
        return obj
    }

}