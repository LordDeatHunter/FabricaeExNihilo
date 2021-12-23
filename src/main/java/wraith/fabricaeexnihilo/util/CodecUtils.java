package wraith.fabricaeexnihilo.util;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;

import java.util.function.Function;


/**
 * Contains {@link Codec}s that provide a more friendly format for end users. They allow specifying only the necessary details in a simple format. Also has utility methods for quickly using codecs.
 */
@SuppressWarnings("UnstableApiUsage")
public class CodecUtils {
    public static final Codec<FluidVariant> FLUID_VARIANT = Codec.either(RecordCodecBuilder.<FluidVariant>create(instance1 -> instance1.group(
                            Registry.FLUID.getCodec()
                                    .fieldOf("type")
                                    .forGetter(FluidVariant::getFluid),
                            NbtCompound.CODEC
                                    .fieldOf("nbt")
                                    .forGetter(TransferVariant::getNbt)).apply(instance1, FluidVariant::of)),
                    Registry.FLUID.getCodec()
                            .xmap(FluidVariant::of, FluidVariant::getFluid))
            .xmap(CodecUtils::flattenEither, variant -> variant.hasNbt() ? Either.left(variant) : Either.right(variant));
    
    public static final Codec<ItemStack> ITEM_STACK = Codec.either(RecordCodecBuilder.<ItemStack>create(instance1 -> instance1.group(
                            Registry.ITEM.getCodec()
                                    .fieldOf("item")
                                    .forGetter(ItemStack::getItem),
                            Codec.INT
                                    .fieldOf("count")
                                    .forGetter(ItemStack::getCount),
                            NbtCompound.CODEC
                                    .fieldOf("nbt")
                                    .forGetter(ItemStack::getNbt)).apply(instance1, (item, count, nbt) -> {
                        var stack = new ItemStack(item);
                        stack.setCount(count);
                        stack.setNbt(nbt);
                        return stack;
                    })),
                    Registry.ITEM.getCodec()
                            .xmap(Item::getDefaultStack, ItemStack::getItem))
            .xmap(CodecUtils::flattenEither, stack -> stack.hasNbt() ? Either.left(stack) : Either.right(stack));
    
    public static <T> T deserializeNbt(Codec<T> codec, NbtElement data) {
        return deserialize(codec, NbtOps.INSTANCE, data);
    }
    
    public static <T> NbtElement serializeNbt(Codec<T> codec, T data) {
        return serialize(codec, NbtOps.INSTANCE, data);
    }
    
    public static <T> T deserializeJson(Codec<T> codec, JsonElement data) {
        return deserialize(codec, JsonOps.INSTANCE, data);
    }
    
    public static <T> JsonElement serializeJson(Codec<T> codec, T data) {
        return serialize(codec, JsonOps.INSTANCE, data);
    }
    
    public static <T, O> T deserialize(Codec<T> codec, DynamicOps<O> ops, O data) {
        return codec.decode(ops, data).getOrThrow(false, FabricaeExNihilo.LOGGER::warn).getFirst();
    }
    
    public static <T, O> O serialize(Codec<T> codec, DynamicOps<O> ops, T data) {
        return codec.encodeStart(ops, data).getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
    }
    
    private static <T> T flattenEither(Either<T, T> either) {
        return either.map(Function.identity(), Function.identity());
    }
}
