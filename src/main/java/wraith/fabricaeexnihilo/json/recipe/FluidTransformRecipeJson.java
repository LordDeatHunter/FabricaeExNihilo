package wraith.fabricaeexnihilo.json.recipe;

import com.google.gson.*;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.recipes.barrel.FluidTransformRecipe;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.EmptyMode;

import java.lang.reflect.Type;

public final class FluidTransformRecipeJson extends BaseJson<FluidTransformRecipe> {

    private FluidTransformRecipeJson() {}

    public static final FluidTransformRecipeJson INSTANCE = new FluidTransformRecipeJson();

    @Override
    public FluidTransformRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var obj = json.getAsJsonObject();
        return new FluidTransformRecipe(
                FluidIngredient.fromJson(obj.get("inBarrel"), context),
                ItemIngredient.fromJson(obj.get("catalyst"), context),
                BarrelMode.BARREL_MODE_FACTORY(obj.get("result"), context)
        );
    }

    @Override
    public JsonElement serialize(FluidTransformRecipe src, Type typeOfSrc, JsonSerializationContext context) {
        var obj = new JsonObject();
        obj.add("inBarrel", src.inBarrel().toJson(context));
        obj.add("catalyst", src.catalyst().toJson(context));
        if(!(src.result() instanceof EmptyMode)) {
            var result = new JsonObject();
            result.add(src.result().nbtKey(), context.serialize(src.result()));
            obj.add("result", result);
        }
        return obj;
    }

}