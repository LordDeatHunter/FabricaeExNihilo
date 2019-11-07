package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.recipes.ToolRecipe
import net.minecraft.recipe.Ingredient
import java.lang.reflect.Type

object ToolRecipeJson: JsonDeserializer<ToolRecipe>, JsonSerializer<ToolRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ToolRecipe {
        return ToolRecipe(
            Ingredient.fromJson(json.asJsonObject.get("ingredient")),
            json.asJsonObject.get("loot").asJsonArray.map { context.deserialize<Lootable>(it, object: TypeToken<Lootable>(){}.type) }.toMutableList()
        )
    }

    override fun serialize(src: ToolRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("ingredient", src.ingredient.toJson())
        obj.add("loot", context.serialize(src.lootables))
        return obj
    }

}