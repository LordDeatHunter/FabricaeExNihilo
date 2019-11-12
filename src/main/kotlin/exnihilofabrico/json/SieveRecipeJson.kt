package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.recipes.SieveRecipe
import java.lang.reflect.Type

object SieveRecipeJson: JsonDeserializer<SieveRecipe>, JsonSerializer<SieveRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SieveRecipe {
        val obj = json.asJsonObject
        return SieveRecipe(
            ItemIngredient.fromJson(obj["mesh"], context),
            if(obj.get("fluid") != null)
                FluidIngredient.fromJson(obj["fluid"], context)
            else
                FluidIngredient.EMPTY
            ,
            ItemIngredient.fromJson(obj["sievable"], context),
            context.deserialize(obj["loot"], object: TypeToken<MutableCollection<Lootable>>(){}.type)
        )
    }

    override fun serialize(src: SieveRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("mesh", src.mesh.toJson(context))
        if(!src.fluid.isEmpty())
            obj.add("fluid", src.fluid.toJson(context))
        obj.add("sievable", src.sievable.toJson(context))
        obj.add("loot", context.serialize(src.loot))
        return obj
    }

}