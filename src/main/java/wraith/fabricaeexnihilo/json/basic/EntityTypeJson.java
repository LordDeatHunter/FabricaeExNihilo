package wraith.fabricaeexnihilo.json.basic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.json.other.IdentifierJson;
import wraith.fabricaeexnihilo.util.RegistryUtils;

import java.lang.reflect.Type;

public final class EntityTypeJson extends BaseJson<EntityType<?>> {

    private EntityTypeJson() {
    }

    public static final EntityTypeJson INSTANCE = new EntityTypeJson();

    @Override
    public EntityType<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Registry.ENTITY_TYPE.get(context.<Identifier>deserialize(json, IdentifierJson.TYPE_TOKEN));
    }

    @Override
    public JsonElement serialize(EntityType<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(RegistryUtils.getId(src));
    }

}