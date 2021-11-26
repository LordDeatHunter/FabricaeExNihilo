package wraith.fabricaeexnihilo.json.basic;

import com.google.gson.*;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.Tag;
import wraith.fabricaeexnihilo.json.BaseJson;

import java.lang.reflect.Type;

public final class EntityTypeTagJson extends BaseJson<Tag.Identified<EntityType<?>>> {

    private EntityTypeTagJson() {
    }

    public static final EntityTypeTagJson INSTANCE = new EntityTypeTagJson();

    @Override
    public Tag.Identified<EntityType<?>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return TagFactory.ENTITY_TYPE.create(getTagID(json.getAsString()));
    }

    @Override
    public JsonElement serialize(Tag.Identified<EntityType<?>> src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive("entity#" + context.serialize(src.getId()).getAsString());
    }

}