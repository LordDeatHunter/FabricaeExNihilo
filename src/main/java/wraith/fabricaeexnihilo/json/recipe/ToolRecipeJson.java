package wraith.fabricaeexnihilo.json.recipe;

import com.google.gson.*;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.crafting.Loot;
import wraith.fabricaeexnihilo.api.recipes.ToolRecipe;
import wraith.fabricaeexnihilo.json.BaseJson;

import java.lang.reflect.Type;
import java.util.stream.StreamSupport;

public final class ToolRecipeJson extends BaseJson<ToolRecipe> {

    private ToolRecipeJson() {}

    public static final ToolRecipeJson INSTANCE = new ToolRecipeJson();

    @Override
    public ToolRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var obj = json.getAsJsonObject();
        return new ToolRecipe(
                ItemIngredient.fromJson(obj.get("ingredient")),
                StreamSupport.stream(obj.get("loot").getAsJsonArray().spliterator(), false).map(loot -> context.<Loot>deserialize(loot, LOOTABLE_TYPE_TOKEN)).toList()
        );
    }

    @Override
    public JsonElement serialize(ToolRecipe src, Type typeOfSrc, JsonSerializationContext context) {
        var obj = new JsonObject();
        obj.add("ingredient", src.ingredient().toJson());
        obj.add("loot", context.serialize(src.loots()));
        return obj;
    }

}
