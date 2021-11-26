package wraith.fabricaeexnihilo.json.basic;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.json.other.IdentifierJson;
import wraith.fabricaeexnihilo.util.RegistryUtils;

import java.lang.reflect.Type;

public final class ItemJson extends BaseJson<Item> {

    private ItemJson() {
    }

    public static final Type TYPE_TOKEN = new TypeToken<Item>() {}.getType();

    public static final ItemJson INSTANCE = new ItemJson();

    @Override
    public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Registry.ITEM.get(context.<Identifier>deserialize(json, IdentifierJson.TYPE_TOKEN));
    }

    @Override
    public JsonElement serialize(Item src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(RegistryUtils.getId(src).toString());
    }

}