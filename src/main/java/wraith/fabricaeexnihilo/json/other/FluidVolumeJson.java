package wraith.fabricaeexnihilo.json.other;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.gson.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.json.BaseJson;
import wraith.fabricaeexnihilo.util.RegistryUtils;

import java.lang.reflect.Type;

public final class FluidVolumeJson extends BaseJson<FluidVolume> {

    private FluidVolumeJson() {}

    public static final FluidVolumeJson INSTANCE = new FluidVolumeJson();

    @Override
    public FluidVolume deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var splits = json.getAsString().split(" x ");
        var amount = Integer.parseInt(splits[0]);
        var key = FluidKeys.get(Registry.FLUID.get(new Identifier(splits[1])));
        return key.withAmount(FluidAmount.of1620(amount));
    }

    @Override
    public JsonElement serialize(FluidVolume src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.amount() + " x " + RegistryUtils.getId(src.getRawFluid()));
    }

}