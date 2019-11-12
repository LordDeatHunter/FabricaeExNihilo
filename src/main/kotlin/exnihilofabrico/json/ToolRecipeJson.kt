package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.recipes.ToolRecipe
import java.lang.reflect.Type


object ToolRecipeJson: JsonDeserializer<ToolRecipe>, JsonSerializer<ToolRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ToolRecipe {
        return ToolRecipe(
            ItemIngredient.fromJson(json.asJsonObject.get("ingredient"), context),
            json.asJsonObject.get("loot").asJsonArray.map { context.deserialize<Lootable>(it, object: TypeToken<Lootable>(){}.type) }.toMutableList()
        )
    }

    override fun serialize(src: ToolRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("ingredient", src.ingredient.toJson(context))
        obj.add("loot", context.serialize(src.lootables))
        return obj
    }

}
