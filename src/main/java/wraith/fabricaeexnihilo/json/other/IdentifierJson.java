package wraith.fabricaeexnihilo.json.other;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.json.BaseJson;

import java.lang.reflect.Type;

public final class IdentifierJson extends BaseJson<Identifier> {

    private IdentifierJson() {}

    public static final Type TYPE_TOKEN = new TypeToken<Identifier>() {}.getType();

    public static final IdentifierJson INSTANCE = new IdentifierJson();

    @Override
    public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return new Identifier(json.getAsJsonPrimitive().getAsString());
    }

    @Override
    public JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

}