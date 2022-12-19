package wraith.fabricaeexnihilo.util;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import wraith.fabricaeexnihilo.FabricaeExNihilo;

import java.util.Optional;
import java.util.function.Function;


/**
 * Contains {@link Codec}s that provide a more friendly format for end users. They allow specifying only the necessary
 * details in a simple format. Also has utility methods for quickly using codecs.
 */
@SuppressWarnings("UnstableApiUsage")
public class CodecUtils {

    public static final Codec<FluidVariant> FLUID_VARIANT = magicCodec(Registries.FLUID.getCodec()
            .xmap(FluidVariant::of, FluidVariant::getFluid),
        RecordCodecBuilder.create(instance1 -> instance1.group(
                Registries.FLUID.getCodec()
                    .fieldOf("type")
                    .forGetter(FluidVariant::getFluid),
                NbtCompound.CODEC
                    .optionalFieldOf("nbt")
                    .forGetter(variant -> Optional.ofNullable(variant.getNbt())))
            .apply(instance1, (fluid, nbt) -> FluidVariant.of(fluid, nbt.orElse(null)))));

    public static final Codec<ItemStack> ITEM_STACK = magicCodec(Registries.ITEM.getCodec()
            .xmap(Item::getDefaultStack, ItemStack::getItem),
        RecordCodecBuilder.create(instance1 -> instance1.group(
            Registries.ITEM.getCodec()
                .fieldOf("item")
                .forGetter(ItemStack::getItem),
            Codec.INT
                .optionalFieldOf("count")
                .forGetter(stack -> Optional.of(stack.getCount())),
            NbtCompound.CODEC
                .optionalFieldOf("nbt")
                .forGetter(itemStack -> Optional.ofNullable(itemStack.getNbt()))).apply(instance1, (item, count, nbt) -> {
            var stack = new ItemStack(item);
            count.ifPresent(stack::setCount);
            nbt.ifPresent(stack::setNbt);
            return stack;
        })));

    private static <T> Codec<T> magicCodec(Codec<T> simple, Codec<T> expanded) {
        return Codec.either(simple, expanded).xmap(either -> either.map(Function.identity(), Function.identity()), Either::right);
    }

    public static <T> T fromPacket(Codec<T> codec, PacketByteBuf buf) {
        var packed = buf.readBoolean();
        NbtCompound nbt = buf.readNbt();
        return fromNbt(codec, packed && nbt != null ? nbt.get("value") : nbt);
    }

    public static <T> void toPacket(Codec<T> codec, T data, PacketByteBuf buf) {
        var nbtData = toNbt(codec, data);
        // Packets only support nbt compounds, so if we have a non-compound value we set a flag and wrap it
        if (!(nbtData instanceof NbtCompound compound)) {
            buf.writeBoolean(true);
            var compound = new NbtCompound();
            compound.put("value", nbtData);
            buf.writeNbt(compound);
            return;
        }
        buf.writeBoolean(false);
        buf.writeNbt(compound);
    }

    public static <T> T fromNbt(Codec<T> codec, NbtElement data) {
        return deserialize(codec, NbtOps.INSTANCE, data);
    }

    public static <T> NbtElement toNbt(Codec<T> codec, T data) {
        return serialize(codec, NbtOps.INSTANCE, data);
    }

    public static <T> T fromJson(Codec<T> codec, JsonElement data) {
        return deserialize(codec, JsonOps.INSTANCE, data);
    }

    public static <T> JsonElement toJson(Codec<T> codec, T data) {
        return serialize(codec, JsonOps.INSTANCE, data);
    }

    public static <T, O> T deserialize(Codec<T> codec, DynamicOps<O> ops, O data) {
        return codec.decode(ops, data).getOrThrow(false, FabricaeExNihilo.LOGGER::warn).getFirst();
    }

    public static <T, O> O serialize(Codec<T> codec, DynamicOps<O> ops, T data) {
        return codec.encodeStart(ops, data).getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
    }

}
