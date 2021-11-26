package wraith.fabricaeexnihilo.json.basic;

import com.google.gson.*;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import wraith.fabricaeexnihilo.json.BaseJson;

import java.lang.reflect.Type;

public final class ItemTagJson extends BaseJson<Tag.Identified<Item>> {

    private ItemTagJson() {
    }

    public static final ItemTagJson INSTANCE = new ItemTagJson();

    @Override
    public Tag.Identified<Item> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return TagFactory.ITEM.create(getTagID(json.getAsString()));
    }

    @Override
    public JsonElement serialize(Tag.Identified<Item> src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive("item#" + context.serialize(src).getAsString());
    }

}