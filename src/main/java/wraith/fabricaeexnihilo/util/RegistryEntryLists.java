package wraith.fabricaeexnihilo.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.*;

import java.util.function.Function;

public class RegistryEntryLists {
    private static final DynamicRegistryManager STATIC_DRM = DynamicRegistryManager.of(Registry.REGISTRIES);

    public static <T> RegistryEntryList<T> fromJson(RegistryKey<Registry<T>> registry, JsonElement json) {
        return CodecUtils.deserialize(RegistryCodecs.entryList(registry), getRegistryOps(JsonOps.INSTANCE), json);
    }

    public static <T> RegistryEntryList<T> fromPacket(RegistryKey<Registry<T>> registry, PacketByteBuf buf) {
        var wrapper = buf.readNbt();
        if (wrapper == null)
            throw new NullPointerException("wraith.fabricaeexnihilo.util.RegistryEntryLists.fromPacket()");
        var nbt = wrapper.get("value");
        return CodecUtils.deserialize(RegistryCodecs.entryList(registry), getRegistryOps(NbtOps.INSTANCE), nbt);
    }

    public static <T> void toPacket(RegistryKey<Registry<T>> registry, RegistryEntryList<T> list, PacketByteBuf buf) {
        var nbt = CodecUtils.serialize(RegistryCodecs.entryList(registry), getRegistryOps(NbtOps.INSTANCE), list);
        var wrapper = new NbtCompound();
        wrapper.put("value", nbt);
        buf.writeNbt(wrapper);
    }

    public static <T> EntryIngredient asReiIngredient(RegistryEntryList<T> list, Function<T, EntryStack<?>> transformer) {
        return EntryIngredient.of(list.stream().map(RegistryEntry::value).map(transformer).toList());
    }

    public static EntryIngredient asReiIngredient(RegistryEntryList<? extends ItemConvertible> list) {
        return EntryIngredient.of(list.stream().map(RegistryEntry::value).map(EntryStacks::of).toList());
    }

    private static <O> RegistryOps<O> getRegistryOps(DynamicOps<O> ops) {
        return RegistryOps.of(ops, STATIC_DRM);
    }
}
