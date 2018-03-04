package exnihilocreatio.json;

import com.google.gson.*;
import exnihilocreatio.util.IStackInfo;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.LogUtil;
import net.minecraft.item.Item;

import java.lang.reflect.Type;

public class CustomStackInfoJson implements JsonDeserializer<IStackInfo>, JsonSerializer<IStackInfo> {
    @Override
    public JsonElement serialize(IStackInfo src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("name", src.getItemStack().getItem().getRegistryName() == null ? "" : src.getItemStack().getItem().getRegistryName().toString());
        obj.addProperty("meta", src.getItemStack().getMetadata());

        return obj;
    }

    @Override
    public IStackInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
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
