package wraith.fabricaeexnihilo.json.other;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import wraith.fabricaeexnihilo.json.BaseJson;

import java.lang.reflect.Type;

@SuppressWarnings("UnstableApiUsage")
public class FluidVariantJson extends BaseJson<FluidVariant> {
    @Override
    public FluidVariant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }
    
    @Override
    public JsonElement serialize(FluidVariant src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
