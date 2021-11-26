package wraith.fabricaeexnihilo.json.basic;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.json.other.IdentifierJson;
import wraith.fabricaeexnihilo.util.RegistryUtils;

import java.lang.reflect.Type;

public final class BlockJson extends BaseJson<Block> {

    private BlockJson() {
    }

    public static final BlockJson INSTANCE = new BlockJson();

    @Override
    public Block deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Registry.BLOCK.get(context.<Identifier>deserialize(json, IdentifierJson.TYPE_TOKEN));
    }

    @Override
    public JsonElement serialize(Block src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(RegistryUtils.getId(src).toString());
    }

}