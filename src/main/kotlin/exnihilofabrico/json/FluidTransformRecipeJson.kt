package exnihilofabrico.json

import com.google.gson.*
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.recipes.barrel.FluidTransformRecipe
import exnihilofabrico.modules.barrels.modes.EmptyMode
import exnihilofabrico.modules.barrels.modes.barrelModeFactory
import java.lang.reflect.Type

object FluidTransformRecipeJson: JsonDeserializer<FluidTransformRecipe>, JsonSerializer<FluidTransformRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): FluidTransformRecipe {
        val obj = json.asJsonObject
        return FluidTransformRecipe(
            FluidIngredient.fromJson(obj["inBarrel"], context),
            ItemIngredient.fromJson(obj["catalyst"], context),
            barrelModeFactory(obj["result"], context)
        )
    }

    override fun serialize(src: FluidTransformRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("inBarrel", src.inBarrel.toJson(context))
        obj.add("catalyst", src.catalyst.toJson(context))
        if(src.result !is EmptyMode) {
            val result = JsonObject()
            result.add(src.result.tagKey(), context.serialize(src.result))
            obj.add("result", result)
        }
        return obj
    }

}