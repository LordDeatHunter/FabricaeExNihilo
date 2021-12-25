package wraith.fabricaeexnihilo.json.ingredient;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import wraith.fabricaeexnihilo.api.crafting.EntityTypeIngredient;
import wraith.fabricaeexnihilo.json.BaseJson;

import java.lang.reflect.Type;

public final class EntityTypeIngredientJson extends BaseJson<EntityTypeIngredient> {

    private EntityTypeIngredientJson() {}

    public static final EntityTypeIngredientJson INSTANCE = new EntityTypeIngredientJson();

    @Override
    public EntityTypeIngredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return EntityTypeIngredient.fromJson(json);
    }

    @Override
    public JsonElement serialize(EntityTypeIngredient src, Type typeOfSrc, JsonSerializationContext context) {
        return src.toJson();
    }

}