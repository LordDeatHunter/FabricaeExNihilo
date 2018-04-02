package exnihilocreatio.json;

import com.google.gson.*;
import exnihilocreatio.registries.types.Meltable;
import exnihilocreatio.util.BlockInfo;

import java.lang.reflect.Type;

public class CustomMeltableJson implements JsonDeserializer<Meltable>, JsonSerializer<Meltable> {
    @Override
    public JsonElement serialize(Meltable src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("fluid", src.getFluid());
        obj.addProperty("amount", src.getAmount());

        if (!src.getTextureOverride().equals(BlockInfo.EMPTY)){
            obj.add("color", context.serialize(src.getTextureOverride(), BlockInfo.class));
        }

        return obj;
    }

    @Override
    public Meltable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonHelper helper = new JsonHelper(json);
        JsonObject obj = json.getAsJsonObject();

        String fluid = helper.getString("fluid");
        int amount = helper.getInteger("amount");

        BlockInfo textureOverride = BlockInfo.EMPTY;
        if (obj.has("textureOverride")) {
            textureOverride = context.deserialize(json.getAsJsonObject().get("textureOverride"), BlockInfo.class);
        }

        return new Meltable(fluid, amount, textureOverride);
    }
}
