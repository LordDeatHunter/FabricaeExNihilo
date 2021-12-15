package wraith.fabricaeexnihilo.json.recipe;

import com.google.gson.*;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.recipes.barrel.FluidOnTopRecipe;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.EmptyMode;

import java.lang.reflect.Type;

public final class FluidOnTopRecipeJson extends BaseJson<FluidOnTopRecipe> {

    private FluidOnTopRecipeJson() {}

    public static final FluidOnTopRecipeJson INSTANCE = new FluidOnTopRecipeJson();

    @Override
    public FluidOnTopRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var obj = json.getAsJsonObject();
        return new FluidOnTopRecipe(
                FluidIngredient.fromJson(obj.get("inBarrel"), context),
                FluidIngredient.fromJson(obj.get("onTop"), context),
                BarrelMode.of(obj.get("result"), context)
        );
    }

    @Override
    public JsonElement serialize(FluidOnTopRecipe src, Type typeOfSrc, JsonSerializationContext context) {
        var obj = new JsonObject();
        obj.add("inBarrel", src.inBarrel().toJson(context));
        obj.add("onTop", src.onTop().toJson(context));
        if(!(src.result() instanceof EmptyMode)) {
            var result = new JsonObject();
            result.add(src.result().nbtKey(), context.serialize(src.result()));
            obj.add("result", result);
        }
        return obj;
    }

}