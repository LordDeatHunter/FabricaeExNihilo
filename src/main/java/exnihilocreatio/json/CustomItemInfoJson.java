package exnihilocreatio.json;

import com.google.gson.*;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.LogUtil;
import net.minecraft.item.Item;

import java.lang.reflect.Type;

public class CustomItemInfoJson implements JsonDeserializer<ItemInfo>, JsonSerializer<ItemInfo> {
    @Override
    public JsonElement serialize(ItemInfo src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getItem().getRegistryName().toString() + ":" + src.getMeta());
    }

    @Override
    public ItemInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            String name = json.getAsString();
            return new ItemInfo(name);
        } else {
            JsonHelper helper = new JsonHelper(json);

            String name = helper.getString("name");
            int meta = helper.getNullableInteger("meta", 0);

            Item item = Item.getByNameOrId(name);

            if (item == null) {
                LogUtil.error("Error parsing JSON: Invalid Item: " + json.toString());
                LogUtil.error("This may result in crashing or other undefined behavior");
            }

            return new ItemInfo(item, meta);
        }
    }
}
