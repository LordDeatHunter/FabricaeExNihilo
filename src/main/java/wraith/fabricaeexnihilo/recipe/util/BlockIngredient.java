package wraith.fabricaeexnihilo.recipe.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Block;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.function.Predicate;

public sealed abstract class BlockIngredient implements Predicate<Block> {
    public static BlockIngredient fromJson(JsonElement json) {
        var data = JsonHelper.asString(json, "block ingredient");
        if (data.startsWith("#"))
            return new Tag(TagKey.of(RegistryKeys.BLOCK, new Identifier(data.substring(1))));
        else
            return new Single(Registries.BLOCK.get(new Identifier(data)));
    }

    public static BlockIngredient fromPacket(PacketByteBuf buf) {
        var id = buf.readByte();
        return switch (id) {
            case 0 -> new Single(buf.readRegistryValue(Registries.BLOCK));
            case 1 -> new Tag(TagKey.of(RegistryKeys.BLOCK, buf.readIdentifier()));
            default -> throw new IllegalStateException("Unexpected block ingredient type: " + id);
        };
    }

    public abstract void toPacket(PacketByteBuf buf);

    public abstract JsonElement toJson();

    public abstract EntryIngredient asReiIngredient();

    private static final class Single extends BlockIngredient {
        private final Block block;

        private Single(Block block) {
            this.block = block;
        }

        @Override
        public boolean test(Block block) {
            return this.block == block;
        }

        @Override
        public void toPacket(PacketByteBuf buf) {
            buf.writeByte(0);
            buf.writeRegistryValue(Registries.BLOCK, block);
        }

        @Override
        public JsonElement toJson() {
            return new JsonPrimitive(Registries.BLOCK.getId(block).toString());
        }

        @Override
        public EntryIngredient asReiIngredient() {
            return EntryIngredients.of(block.asItem());
        }
    }

    private static final class Tag extends BlockIngredient {
        private final TagKey<Block> tag;

        private Tag(TagKey<Block> tag) {
            this.tag = tag;
        }

        @Override
        public boolean test(Block block) {
            return block.getRegistryEntry().isIn(tag);
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
        public EntryIngredient asReiIngredient() {
            return EntryIngredients.ofTag(tag, block -> EntryStacks.of(block.value().asItem(), 1));
        }
    }
}
