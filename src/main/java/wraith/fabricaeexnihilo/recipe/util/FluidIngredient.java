package wraith.fabricaeexnihilo.recipe.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Either;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public sealed abstract class FluidIngredient implements Predicate<Fluid> {
    public static FluidIngredient fromJson(JsonElement json) {
        var data = JsonHelper.asString(json, "fluid ingredient");
        if (data.startsWith("#"))
            return new Tag(TagKey.of(RegistryKeys.FLUID, new Identifier(data.substring(1))));
        else
            return new Single(Registries.FLUID.get(new Identifier(data)));
    }

    public static FluidIngredient fromPacket(PacketByteBuf buf) {
        var id = buf.readByte();
        return switch (id) {
            case 0 -> new Single(buf.readRegistryValue(Registries.FLUID));
            case 1 -> new Tag(TagKey.of(RegistryKeys.FLUID, buf.readIdentifier()));
            default -> throw new IllegalStateException("Unexpected fluid ingredient type: " + id);
        };
    }

    public static FluidIngredient single(Fluid fluid) {
        return new Single(fluid);
    }

    public static FluidIngredient tag(TagKey<Fluid> tag) {
        return new Tag(tag);
    }

    public abstract Either<Fluid, TagKey<Fluid>> getValue();

    public abstract void toPacket(PacketByteBuf buf);

    public abstract JsonElement toJson();

    public abstract boolean isEmpty();

    private static final class Single extends FluidIngredient {
        private final Fluid fluid;

        private Single(Fluid fluid) {
            this.fluid = fluid;
        }

        @Override
        public boolean test(Fluid fluid) {
            return fluid.matchesType(this.fluid);
        }

        @Override
        public Either<Fluid, TagKey<Fluid>> getValue() {
            return Either.left(fluid);
        }

        @Override
        public void toPacket(PacketByteBuf buf) {
            buf.writeByte(0);
            buf.writeRegistryValue(Registries.FLUID, fluid);
        }

        @Override
        public JsonElement toJson() {
            return new JsonPrimitive(Registries.FLUID.getId(fluid).toString());
        }

        @Override
        public boolean isEmpty() {
            return fluid == Fluids.EMPTY;
        }
    }

    private static final class Tag extends FluidIngredient {
        private final TagKey<Fluid> tag;

        private Tag(TagKey<Fluid> tag) {
            this.tag = tag;
        }

        @Override
        public boolean test(Fluid fluid) {
            return fluid.isIn(tag);
        }

        @Override
        public Either<Fluid, TagKey<Fluid>> getValue() {
            return Either.right(tag);
        }

        @Override
        public void toPacket(PacketByteBuf buf) {
            buf.writeByte(1);
            buf.writeIdentifier(tag.id());
        }

        @Override
        public JsonElement toJson() {
            return new JsonPrimitive("#" + tag.id().toString());
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}
