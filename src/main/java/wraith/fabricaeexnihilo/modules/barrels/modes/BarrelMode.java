package wraith.fabricaeexnihilo.modules.barrels.modes;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import net.minecraft.nbt.NbtCompound;

public interface BarrelMode {

    NbtCompound writeNbt();
    String nbtKey();

    static BarrelMode of(NbtCompound nbt) {
        if (nbt.contains("item_mode")) {
            return ItemMode.readNbt(nbt.getCompound("item_mode"));
        }
        if (nbt.contains("fluid_mode")) {
            return FluidMode.readNbt(nbt.getCompound("fluid_mode"));
        }
        if (nbt.contains("alchemy_mode")) {
            return AlchemyMode.readNbt(nbt.getCompound("alchemy_mode"));
        }
        if (nbt.contains("compost_mode")) {
            return CompostMode.readNbt(nbt.getCompound("compost_mode"));
        }
        return new EmptyMode();
    }

    static BarrelMode of(JsonElement json, JsonDeserializationContext context) {
        var obj = json.getAsJsonObject();
        if (obj.has("fluid_mode")) {
            return context.deserialize(obj.get("fluid_mode"), new TypeToken<FluidMode>(){}.getType());
        }
        if (obj.has("item_mode")) {
            return context.deserialize(obj.get("item_mode"), new TypeToken<ItemMode>(){}.getType());
        }
        if (obj.has("alchemy_mode")) {
            return context.deserialize(obj.get("alchemy_mode"), new TypeToken<AlchemyMode>(){}.getType());
        }
        if (obj.has("compost_mode")) {
            return context.deserialize(obj.get("compost_mode"), new TypeToken<CompostMode>(){}.getType());
        }
        return new EmptyMode();
    }

}