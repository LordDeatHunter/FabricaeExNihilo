package wraith.fabricaeexnihilo.json.other;

import com.google.gson.*;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.modules.sieves.MeshProperties;

import java.lang.reflect.Type;

public final class MeshPropertiesJson extends BaseJson<MeshProperties> {

    private MeshPropertiesJson() {}

    public static final MeshPropertiesJson INSTANCE = new MeshPropertiesJson();

    @Override
    public MeshProperties deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var obj = json.getAsJsonObject();
        return new MeshProperties(context.deserialize(obj.get("id"), IdentifierJson.INSTANCE.getTypeToken()),
                obj.has("enchantability") ? obj.get("enchantability").getAsInt() : 0,
                obj.get("displayName").getAsString(),
                context.deserialize(obj.get("color"), ColorJson.INSTANCE.getTypeToken()),
                context.deserialize(obj.get("keyIngredient"), IdentifierJson.INSTANCE.getTypeToken()));
    }

    @Override
    public JsonElement serialize(MeshProperties src, Type typeOfSrc, JsonSerializationContext context) {
        var json = new JsonObject();
        json.add("id", context.serialize(src.getIdentifier()));
        json.add("enchantability", context.serialize(src.getEnchantability()));
        json.add("displayName", context.serialize(src.getDisplayName()));
        json.add("color", context.serialize(src.getColor()));
        json.add("keyIngredient", context.serialize(src.getKeyIngredient()));
        return json;
    }

}
