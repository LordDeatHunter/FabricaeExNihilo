package exnihilocreatio.json

import com.google.gson.*
import exnihilocreatio.registries.ingredient.IngredientUtil
import exnihilocreatio.registries.ingredient.OreIngredientStoring
import exnihilocreatio.util.LogUtil
import net.minecraft.item.crafting.Ingredient
import java.lang.reflect.Type

object CustomIngredientJson : JsonDeserializer<Ingredient>, JsonSerializer<Ingredient> {


    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Ingredient {
        if (json.isJsonPrimitive && (json as JsonPrimitive).isString) {
            val s = json.getAsString()
            return IngredientUtil.parseFromString(s)
        } else {
            LogUtil.error("Error parsing JSON: No Primitive String: $json")
        }

        return Ingredient.EMPTY
    }

    override fun serialize(src: Ingredient, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        if (src is OreIngredientStoring) {
            return JsonPrimitive("ore:" + src.oreName)
        } else {
            val stacks = src.getMatchingStacks()
            if (stacks.isNotEmpty())
                return JsonPrimitive(stacks[0].item.registryName!!.toString() + ":" + stacks[0].metadata)
        }

        return JsonPrimitive("minecraft:air:0")
    }
}
