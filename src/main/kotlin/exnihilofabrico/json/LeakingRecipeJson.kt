package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.recipes.barrel.LeakingRecipe
import java.lang.reflect.Type

object LeakingRecipeJson: JsonDeserializer<LeakingRecipe>, JsonSerializer<LeakingRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LeakingRecipe {
        val obj = json.asJsonObject
        return LeakingRecipe(
            ItemIngredient.fromJson(obj["target"], context),
            FluidIngredient.fromJson(obj["fluid"], context),
            context.deserialize(obj["loss"], object : TypeToken<Int>() {}.type),
            BlockJson.deserialize(obj["result"], BLOCK_TYPE_TOKEN, context)
        )
    }

    override fun serialize(src: LeakingRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("target", src.target.toJson(context))
        obj.add("fluid", src.fluid.toJson(context))
        obj.add("loss", context.serialize(src.loss))
        obj.add("result", BlockJson.serialize(src.result, BLOCK_TYPE_TOKEN, context))
        return obj
    }

}