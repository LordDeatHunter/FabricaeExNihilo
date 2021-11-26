package wraith.fabricaeexnihilo.json.ingredient;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.json.BaseJson;

import java.lang.reflect.Type;

public final class ItemIngredientJson extends BaseJson<ItemIngredient> {

    private ItemIngredientJson() {}

    public static final ItemIngredientJson INSTANCE = new ItemIngredientJson();

    @Override
    public ItemIngredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ItemIngredient.fromJson(json, context);
    }

    @Override
    public JsonElement serialize(ItemIngredient src, Type typeOfSrc, JsonSerializationContext context) {
        return src.toJson(context);
    }

}
