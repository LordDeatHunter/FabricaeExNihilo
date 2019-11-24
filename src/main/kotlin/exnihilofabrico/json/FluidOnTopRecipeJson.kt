package exnihilofabrico.json

import com.google.gson.*
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.recipes.barrel.FluidOnTopRecipe
import exnihilofabrico.modules.barrels.modes.EmptyMode
import exnihilofabrico.modules.barrels.modes.barrelModeFactory
import java.lang.reflect.Type

object FluidOnTopRecipeJson: JsonDeserializer<FluidOnTopRecipe>, JsonSerializer<FluidOnTopRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): FluidOnTopRecipe {
        val obj = json.asJsonObject
        return FluidOnTopRecipe(
            FluidIngredient.fromJson(obj["inBarrel"], context),
            FluidIngredient.fromJson(obj["onTop"], context),
            barrelModeFactory(obj["result"], context)
        )
    }

    override fun serialize(src: FluidOnTopRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("inBarrel", src.inBarrel.toJson(context))
        obj.add("onTop", src.onTop.toJson(context))
        if(src.result !is EmptyMode) {
            val result = JsonObject()
            result.add(src.result.tagKey(), context.serialize(src.result))
            obj.add("result", result)
        }
        return obj
    }

}