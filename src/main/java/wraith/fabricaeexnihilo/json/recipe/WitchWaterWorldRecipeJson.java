package wraith.fabricaeexnihilo.json.recipe;

import com.google.gson.*;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.recipes.witchwater.WitchWaterWorldRecipe;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.json.other.WeightedListJson;

import java.lang.reflect.Type;

public final class WitchWaterWorldRecipeJson extends BaseJson<WitchWaterWorldRecipe> {

    private WitchWaterWorldRecipeJson() {}

    public static final WitchWaterWorldRecipeJson INSTANCE = new WitchWaterWorldRecipeJson();

    @Override
    public WitchWaterWorldRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new WitchWaterWorldRecipe(
                FluidIngredient.fromJson(json.getAsJsonObject().get("fluid")),
                WeightedListJson.INSTANCE.deserialize(json.getAsJsonObject().get("results"), WeightedListJson.INSTANCE.getTypeToken(), context)
        );
    }

    @Override
    public JsonElement serialize(WitchWaterWorldRecipe src, Type typeOfSrc, JsonSerializationContext context) {
        var obj = new JsonObject();
        obj.add("fluid", src.fluid().toJson());
        obj.add("results", WeightedListJson.INSTANCE.serialize(src.results(), WeightedListJson.INSTANCE.getTypeToken(), context));
        return obj;
    }

}
