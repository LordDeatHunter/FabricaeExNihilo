package wraith.fabricaeexnihilo.json.ingredient;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.json.BaseJson;

import java.lang.reflect.Type;

public final class FluidIngredientJson extends BaseJson<FluidIngredient> {

    private FluidIngredientJson() {}

    public static final FluidIngredientJson INSTANCE = new FluidIngredientJson();

    @Override
    public FluidIngredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return FluidIngredient.fromJson(json, context);
    }


    @Override
    public JsonElement serialize(FluidIngredient src, Type typeOfSrc, JsonSerializationContext context) {
        return src.toJson(context);
    }

}
