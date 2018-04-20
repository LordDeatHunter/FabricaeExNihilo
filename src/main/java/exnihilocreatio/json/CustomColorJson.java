package exnihilocreatio.json;

import com.google.gson.*;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.LogUtil;

import java.lang.reflect.Type;

public class CustomColorJson implements JsonDeserializer<Color>, JsonSerializer<Color> {
    @Override
    public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) {
            JsonPrimitive prim = json.getAsJsonPrimitive();
            if (prim.isString()) {
                return new Color(prim.getAsString());
            } else if (prim.isNumber()) {
                return new Color(prim.getAsInt());
            } else {
                LogUtil.warn("Invalid Color primitive for " + json.toString());
            }
        } else {
            JsonHelper helper = new JsonHelper(json);
            return new Color((float)helper.getDouble("r"), (float)helper.getDouble("g"), (float)helper.getDouble("b"), (float)helper.getDouble("a"));
        }

        return Color.INVALID_COLOR;
    }

    @Override
    public JsonElement serialize(Color src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getAsHexNoAlpha());
    }
}
