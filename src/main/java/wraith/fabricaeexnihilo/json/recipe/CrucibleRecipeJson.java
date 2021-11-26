package wraith.fabricaeexnihilo.json.recipe;

import com.google.gson.*;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.recipes.crucible.CrucibleRecipe;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.json.other.FluidVolumeJson;

import java.lang.reflect.Type;

public final class CrucibleRecipeJson extends BaseJson<CrucibleRecipe> {

    private CrucibleRecipeJson() {}

    public static final CrucibleRecipeJson INSTANCE = new CrucibleRecipeJson();

    @Override
    public CrucibleRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var obj = json.getAsJsonObject();
        return new CrucibleRecipe(
                ItemIngredient.fromJson(obj.get("input"), context),
                FluidVolumeJson.INSTANCE.deserialize(obj.get("output"), FluidVolumeJson.INSTANCE.getTypeToken(), context)
        );
    }

    @Override
    public JsonElement serialize(CrucibleRecipe src, Type typeOfSrc, JsonSerializationContext context) {
        var obj = new JsonObject();
        obj.add("input", src.input().toJson(context));
        obj.add("output", FluidVolumeJson.INSTANCE.serialize(src.output(), FluidVolumeJson.INSTANCE.getTypeToken(), context));
        return obj;
    }

}