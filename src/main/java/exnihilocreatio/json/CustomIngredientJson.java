package exnihilocreatio.json;

import com.google.gson.*;
import exnihilocreatio.registries.ingredient.IngredientUtil;
import exnihilocreatio.registries.ingredient.OreIngredientStoring;
import exnihilocreatio.util.LogUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.lang.reflect.Type;

public class CustomIngredientJson implements JsonDeserializer<Ingredient>, JsonSerializer<Ingredient> {


    @Override
    public Ingredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive() && ((JsonPrimitive)json).isString()){
            String s = json.getAsString();
            return IngredientUtil.parseFromString(s);
        } else {
            LogUtil.error("Error parsing JSON: No Primitive String: " + json.toString());
        }

        return Ingredient.EMPTY;
    }

    @Override
    public JsonElement serialize(Ingredient src, Type typeOfSrc, JsonSerializationContext context) {
        if (src instanceof OreIngredientStoring) {
            OreIngredientStoring ore = (OreIngredientStoring) src;
            return new JsonPrimitive("ore:" + ore.getOreName());
        } else {
            ItemStack[] stacks = src.getMatchingStacks();
            if (stacks.length > 0)
                return new JsonPrimitive(stacks[0].getItem().getRegistryName().toString() + ":" + stacks[0].getMetadata());
        }

        return new JsonPrimitive("minecraft:air:0");
    }
}
