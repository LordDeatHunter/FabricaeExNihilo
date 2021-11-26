package wraith.fabricaeexnihilo.json.other;

import com.google.gson.*;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.util.Color;

import java.lang.reflect.Type;

public final class ColorJson extends BaseJson<Color> {

    private ColorJson() {}

    public static final ColorJson INSTANCE = new ColorJson();

    @Override
    public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Color(json.getAsString());
    }

    @Override
    public JsonElement serialize(Color src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toHex());
    }


}
