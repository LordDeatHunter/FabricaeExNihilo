package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.TagIngredient
import exnihilofabrico.api.recipes.WitchWaterEntityRecipe
import net.minecraft.entity.EntityType
import net.minecraft.village.VillagerProfession
import java.lang.reflect.Type

object WitchWaterEntityRecipeJson: JsonDeserializer<WitchWaterEntityRecipe>, JsonSerializer<WitchWaterEntityRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): WitchWaterEntityRecipe {
        val obj = json.asJsonObject
        return WitchWaterEntityRecipe(
            TagIngredient.fromJson(obj["target"], context),
            if(obj.has("profession"))
                VillagerProfessionJson.deserialize(obj["profession"], VillagerProfessionJson.TYPE_TOKEN, context)
            else
                null,
            context.deserialize(obj["spawn"], ENTITY_TYPE_TOKEN)
        )
    }

    override fun serialize(src: WitchWaterEntityRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("target", src.target.toJson<EntityType<*>>(context))
        if(src.profession != null)
            obj.add("profession", context.serialize(src.profession))
        obj.add("spawn", context.serialize(src.tospawn))
        return obj
    }

}