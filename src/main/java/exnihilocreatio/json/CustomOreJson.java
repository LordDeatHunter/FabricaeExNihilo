package exnihilocreatio.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.items.ore.Ore;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.ItemInfo;

import java.lang.reflect.Type;
import java.util.HashMap;

public class CustomOreJson implements JsonDeserializer<Ore>, JsonSerializer<Ore> {
    @Override
    public JsonElement serialize(Ore src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", src.getName());
        obj.add("color", context.serialize(src.getColor(), Color.class));
        obj.add("result", context.serialize(src.getResult(), ItemInfo.class));

        if (src.getOredictName() != null){
            obj.addProperty("oredictName", src.getOredictName());
        }

        if (src.getTranslations() != null){
            obj.add("translations", context.serialize(src.getTranslations(), new TypeToken<HashMap<String, String>>() {}.getType()));
        }

        return obj;
    }

    @Override
    public Ore deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonHelper helper = new JsonHelper(json);

        String name = helper.getString("name");
        Color color = context.deserialize(json.getAsJsonObject().get("color"), Color.class);
        ItemInfo result = context.deserialize(json.getAsJsonObject().get("result"), ItemInfo.class);
        if (result.getMeta() == -1)
            result.setMeta(0);

        HashMap<String, String> translations = null;
        if (json.isJsonObject() && json.getAsJsonObject().has("translations")) {
            translations = context.deserialize(json.getAsJsonObject().get("translations"), new TypeToken<HashMap<String, String>>() {}.getType());
        }

        String oredictName = null;
        if (json.isJsonObject() && json.getAsJsonObject().has("oredictName")) {
            oredictName = helper.getString("oredictName");
        }

        return new Ore(name, color, result, translations, oredictName);
    }
}
