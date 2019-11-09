package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.crafting.TagIngredient
import exnihilofabrico.api.recipes.SieveRecipe
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import java.lang.reflect.Type

object SieveRecipeJson: JsonDeserializer<SieveRecipe>, JsonSerializer<SieveRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SieveRecipe {
        val obj = json.asJsonObject
        return SieveRecipe(
            TagIngredient.fromJson(obj["mesh"], context),
            if(obj.get("fluid") != null)
                TagIngredient.fromJson(obj["fluid"], context)
            else
                null
            ,
            TagIngredient.fromJson(obj["sievable"], context),
            context.deserialize(obj["loot"], object: TypeToken<MutableCollection<Lootable>>(){}.type)
        )
    }

    override fun serialize(src: SieveRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("mesh", src.mesh.toJson<Item>(context))
        if(src.fluid != null)
            obj.add("fluid", src.fluid.toJson<Fluid>(context))
        obj.add("sievable", src.sievable.toJson<Item>(context))
        obj.add("loot", context.serialize(src.loot))
        return obj
    }

}