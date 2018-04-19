package exnihilocreatio.json;

import com.google.gson.*;
import exnihilocreatio.util.EntityInfo;

import java.lang.reflect.Type;

public class CustomEntityInfoJson implements JsonDeserializer<EntityInfo>, JsonSerializer<EntityInfo> {
    @Override
    public JsonElement serialize(EntityInfo src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getName());
    }

    @Override
    public EntityInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String name = json.getAsString();
        return new EntityInfo(name);
    }
}
