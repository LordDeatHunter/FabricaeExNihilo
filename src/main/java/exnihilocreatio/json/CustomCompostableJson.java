package exnihilocreatio.json;

import com.google.gson.*;
import exnihilocreatio.registries.types.Compostable;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.StackInfo;

import java.lang.reflect.Type;

public class CustomCompostableJson implements JsonDeserializer<Compostable>, JsonSerializer<Compostable> {
    @Override
    public JsonElement serialize(Compostable src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("value", src.getValue());
        obj.add("compostBlock", context.serialize(src.getCompostBlock(), StackInfo.class));
        if (!src.getColor().equals(Color.INVALID_COLOR)){
            obj.add("color", context.serialize(src.getColor(), Color.class));
        }

        return obj;
    }

    @Override
    public Compostable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonHelper helper = new JsonHelper(json);
        JsonObject obj = json.getAsJsonObject();

        Float value = (float)helper.getDouble("value");
        Color color = Color.INVALID_COLOR;
        if (obj.has("color")) {
            color = context.deserialize(json.getAsJsonObject().get("color"), Color.class);
        }

        BlockInfo result = context.deserialize(json.getAsJsonObject().get("compostBlock"), BlockInfo.class);
        if (result.getMeta() == -1)
            result.setMeta(0);


        return new Compostable(value, color, result);
    }
}
