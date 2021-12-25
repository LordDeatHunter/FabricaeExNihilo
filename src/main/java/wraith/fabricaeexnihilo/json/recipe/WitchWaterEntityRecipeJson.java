package wraith.fabricaeexnihilo.json.recipe;

import com.google.gson.*;
import wraith.fabricaeexnihilo.api.crafting.EntityTypeIngredient;
import wraith.fabricaeexnihilo.api.recipes.witchwater.WitchWaterEntityRecipe;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.json.basic.EntityTypeJson;
import wraith.fabricaeexnihilo.json.basic.VillagerProfessionJson;

import java.lang.reflect.Type;

public final class WitchWaterEntityRecipeJson extends BaseJson<WitchWaterEntityRecipe> {

    private WitchWaterEntityRecipeJson() {}

    public static final WitchWaterEntityRecipeJson INSTANCE = new WitchWaterEntityRecipeJson();

    @Override
    public WitchWaterEntityRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var obj = json.getAsJsonObject();
        return new WitchWaterEntityRecipe(EntityTypeIngredient.fromJson(obj.get("target")),
                obj.has("profession") ? VillagerProfessionJson.INSTANCE.deserialize(obj.get("profession"), VillagerProfessionJson.INSTANCE.getTypeToken(), context) : null,
                context.deserialize(obj.get("spawn"), EntityTypeJson.INSTANCE.getTypeToken())
        );
    }

    @Override
    public JsonElement serialize(WitchWaterEntityRecipe src, Type typeOfSrc, JsonSerializationContext context) {
        var obj = new JsonObject();
        obj.add("target", src.target().toJson());
        if (src.profession() != null) {
            obj.add("profession", context.serialize(src.profession()));
        }
        obj.add("spawn", context.serialize(src.toSpawn()));
        return obj;
    }

}

