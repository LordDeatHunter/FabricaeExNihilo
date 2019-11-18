package exnihilofabrico.json

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.EntityStack
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.recipes.barrel.AlchemyRecipe
import exnihilofabrico.modules.barrels.modes.EmptyMode
import exnihilofabrico.modules.barrels.modes.barrelModeFactory
import java.lang.reflect.Type

object AlchemyRecipeJson: JsonDeserializer<AlchemyRecipe>, JsonSerializer<AlchemyRecipe> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): AlchemyRecipe {
        val obj = json.asJsonObject
        return AlchemyRecipe(
            FluidIngredient.fromJson(obj.get("reactant"), context),
            ItemIngredient.fromJson(obj.get("catalyst"), context),
            if (!obj.has("product"))
                EmptyMode()
            else
                barrelModeFactory(obj.get("product"), context),
            if (!obj.has("byproduct"))
                Lootable.EMPTY
            else
                context.deserialize<Lootable>(obj["byproduct"], object : TypeToken<Lootable>() {}.type),
            obj["delay"].asInt,
            if (!obj.has("toSpawn"))
                EntityStack.EMPTY
            else
                context.deserialize(obj["toSpawn"], object : TypeToken<EntityStack>() {}.type)
        )
    }

    override fun serialize(src: AlchemyRecipe, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("reactant", src.reactant.toJson(context))
        obj.add("catalyst", src.catalyst.toJson(context))
        if(src.product !is EmptyMode) {
            val product = JsonObject()
            product.add(src.product.tagKey(), context.serialize(src.product))
            obj.add("product", product)
        }
        if(!src.byproduct.isEmpty())
            obj.add("byproduct", context.serialize(src.byproduct))
        obj.add("delay", JsonPrimitive(src.delay))
        if(!src.toSpawn.isEmpty())
            obj.add("toSpawn", context.serialize(src.toSpawn, object: TypeToken<EntityStack>(){}.type))
        return obj
    }

}