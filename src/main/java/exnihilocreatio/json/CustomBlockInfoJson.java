package exnihilocreatio.json;

import com.google.gson.*;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.lang.reflect.Type;

public class CustomBlockInfoJson implements JsonDeserializer<BlockInfo>, JsonSerializer<BlockInfo> {
    @Override
    public JsonElement serialize(BlockInfo src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getBlock().getRegistryName().toString() + ":" + src.getMeta());
    }

    @Override
    public BlockInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            String name = json.getAsString();
            return new BlockInfo(name);
        } else {
            JsonHelper helper = new JsonHelper(json);

            String name = helper.getString("name");
            int meta = helper.getNullableInteger("meta", 0);

            Block block = Block.getBlockFromName(name);

            if (block == null) {
                LogUtil.error("Error parsing JSON: Invalid BlockStoneAxle: " + json.toString());
                LogUtil.error("This may result in crashing or other undefined behavior");
                return BlockInfo.EMPTY;
            }

            return new BlockInfo(block, meta);
        }
    }
}
