package wraith.fabricaeexnihilo.json.basic;

import com.google.gson.*;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import wraith.fabricaeexnihilo.json.BaseJson;

import java.lang.reflect.Type;

public final class BlockTagJson extends BaseJson<Tag.Identified<Block>> {

    private BlockTagJson() {
    }

    public static final BlockTagJson INSTANCE = new BlockTagJson();

    @Override
    public Tag.Identified<Block> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return TagFactory.BLOCK.create(getTagID(json.getAsString()));
    }

    @Override
    public JsonElement serialize(Tag.Identified<Block> src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive("block#" + context.serialize(src).getAsString());
    }

}