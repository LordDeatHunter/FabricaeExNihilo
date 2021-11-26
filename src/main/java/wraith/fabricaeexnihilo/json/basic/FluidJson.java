package wraith.fabricaeexnihilo.json.basic;

import com.google.gson.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.json.other.IdentifierJson;
import wraith.fabricaeexnihilo.util.RegistryUtils;

import java.lang.reflect.Type;

public final class FluidJson implements JsonDeserializer<Fluid>, JsonSerializer<Fluid> {

    private FluidJson() {
    }

    public static final FluidJson INSTANCE = new FluidJson();

    @Override
    public Fluid deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Registry.FLUID.get(context.<Identifier>deserialize(json, IdentifierJson.TYPE_TOKEN));
    }

    @Override
    public JsonElement serialize(Fluid src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(RegistryUtils.getId(src).toString());
    }

}