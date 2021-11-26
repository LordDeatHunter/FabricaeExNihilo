package wraith.fabricaeexnihilo.json.other;

import com.google.gson.*;
import net.minecraft.block.Block;
import wraith.fabricaeexnihilo.api.crafting.WeightedList;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.json.basic.BlockJson;
import wraith.fabricaeexnihilo.util.RegistryUtils;

import java.lang.reflect.Type;
import java.util.HashMap;

public final class WeightedListJson extends BaseJson<WeightedList> {

    private WeightedListJson() {}

    public static final WeightedListJson INSTANCE = new WeightedListJson();

    @Override
    public WeightedList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        HashMap<Block, Integer> map = new HashMap<>();
        for (var entry : json.getAsJsonObject().entrySet()) {
            map.put(BlockJson.INSTANCE.deserialize(new JsonPrimitive(entry.getKey()), BlockJson.INSTANCE.getTypeToken(), context), entry.getValue().getAsInt());
        }
        return new WeightedList(map);
    }

    @Override
    public JsonElement serialize(WeightedList src, Type typeOfSrc, JsonSerializationContext context) {
        var json = new JsonObject();
        src.getValues().forEach((k,v) -> json.add(RegistryUtils.getId(k).toString(), new JsonPrimitive(v)));
        return json;
    }

}