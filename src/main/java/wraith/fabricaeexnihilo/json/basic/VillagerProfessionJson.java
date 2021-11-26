package wraith.fabricaeexnihilo.json.basic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.json.other.IdentifierJson;

import java.lang.reflect.Type;

public final class VillagerProfessionJson extends BaseJson<VillagerProfession> {

    private VillagerProfessionJson() {
    }

    public static final VillagerProfessionJson INSTANCE = new VillagerProfessionJson();


    @Override
    public VillagerProfession deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return Registry.VILLAGER_PROFESSION.get(context.<Identifier>deserialize(json, IdentifierJson.TYPE_TOKEN));
    }

    @Override
    public JsonElement serialize(VillagerProfession src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(context.serialize(src.getId()).getAsString());
    }

}