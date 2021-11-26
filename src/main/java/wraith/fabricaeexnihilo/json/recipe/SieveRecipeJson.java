package wraith.fabricaeexnihilo.json.recipe;

import com.google.gson.*;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.recipes.SieveRecipe;
import wraith.fabricaeexnihilo.json.BaseJson;

import java.lang.reflect.Type;

public final class SieveRecipeJson extends BaseJson<SieveRecipe> {

    private SieveRecipeJson() {}

    public static final SieveRecipeJson INSTANCE = new SieveRecipeJson();

    @Override
    public SieveRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var obj = json.getAsJsonObject();
        return new SieveRecipe(
                ItemIngredient.fromJson(obj.get("mesh"), context),
                obj.get("fluid") != null ? FluidIngredient.fromJson(obj.get("fluid"), context) : FluidIngredient.EMPTY,
                ItemIngredient.fromJson(obj.get("sievable"), context),
                context.deserialize(obj.get("loot"), LOOTABLE_LIST_TYPE_TOKEN)
        );
    }

    @Override
    public JsonElement serialize(SieveRecipe src, Type typeOfSrc, JsonSerializationContext context) {
        var obj = new JsonObject();
        obj.add("mesh", src.mesh().toJson(context));
        if(!src.fluid().isEmpty()) {
            obj.add("fluid", src.fluid().toJson(context));
        }
        obj.add("sievable", src.sievable().toJson(context));
        obj.add("loot", context.serialize(src.loot()));
        return obj;
    }

}