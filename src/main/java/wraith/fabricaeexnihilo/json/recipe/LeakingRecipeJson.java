package wraith.fabricaeexnihilo.json.recipe;

import com.google.gson.*;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.recipes.barrel.LeakingRecipe;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.json.basic.BlockJson;

import java.lang.reflect.Type;

public final class LeakingRecipeJson extends BaseJson<LeakingRecipe> {

    private LeakingRecipeJson() {}
    
    public static final LeakingRecipeJson INSTANCE = new LeakingRecipeJson();

    @Override
    public LeakingRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var obj = json.getAsJsonObject();
        return new LeakingRecipe(
                ItemIngredient.fromJson(obj.get("target"), context),
                FluidIngredient.fromJson(obj.get("fluid"), context),
                context.deserialize(obj.get("loss"), INT_TYPE_TOKEN),
                BlockJson.INSTANCE.deserialize(obj.get("result"), BlockJson.INSTANCE.getTypeToken(), context)
        );
    }

    @Override
    public JsonElement serialize(LeakingRecipe src, Type typeOfSrc, JsonSerializationContext context) {
        var obj = new JsonObject();
        obj.add("target", src.target().toJson(context));
        obj.add("fluid", src.fluid().toJson(context));
        obj.add("loss", context.serialize(src.loss()));
        obj.add("result", BlockJson.INSTANCE.serialize(src.result(), BlockJson.INSTANCE.getTypeToken(), context));
        return obj;
    }

}