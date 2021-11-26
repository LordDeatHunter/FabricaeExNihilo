package wraith.fabricaeexnihilo.json;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.api.crafting.EntityStack;
import wraith.fabricaeexnihilo.api.crafting.Lootable;

import java.lang.reflect.Type;
import java.util.List;

public abstract class BaseJson<T> implements JsonDeserializer<T>, JsonSerializer<T> {

    private final Type typeToken;

    protected static final Type LOOTABLE_TYPE_TOKEN = new TypeToken<Lootable>(){}.getType();
    protected static final Type ENTITY_STACK_TYPE_TOKEN = new TypeToken<EntityStack>() {}.getType();
    protected static final Type LOOTABLE_LIST_TYPE_TOKEN = new TypeToken<List<Lootable>>(){}.getType();
    protected static final Type INT_TYPE_TOKEN = new TypeToken<Integer>() {}.getType();

    protected BaseJson() {
        this.typeToken = new TypeToken<T>() {}.getType();
    }

    public Type getTypeToken() {
        return this.typeToken;
    }

    protected Identifier getTagID(String string) {
        var segments = string.split("#");
        return new Identifier(segments[segments.length - 1]);
    }

}