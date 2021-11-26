package wraith.fabricaeexnihilo.json.basic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import wraith.fabricaeexnihilo.json.BaseJson;

import java.lang.reflect.Type;

public final class FluidTagJson extends BaseJson<Tag.Identified<Fluid>> {

    private FluidTagJson() {
    }

    public FluidTagJson INSTANCE = new FluidTagJson();

    @Override
    public Tag.Identified<Fluid> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return TagFactory.FLUID.create(getTagID(json.getAsString()));
    }

    @Override
    public JsonElement serialize(Tag.Identified<Fluid> src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive("fluid#" + context.serialize(src.getId()).getAsString());
    }

}