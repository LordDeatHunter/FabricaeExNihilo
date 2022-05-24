package wraith.fabricaeexnihilo.recipe.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtString;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;

import java.util.function.Function;

public class BlockIngredient extends AbstractIngredient<Block> {

    public static final Codec<BlockIngredient> CODEC = Codec.PASSTHROUGH
        .xmap(dynamic -> {
            var string = dynamic.asString().getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
            if (string.startsWith("#")) {
                return new BlockIngredient(TagKey.of(Registry.BLOCK_KEY, new Identifier(string.substring(1))));
            } else {
                return new BlockIngredient(Registry.BLOCK.get(new Identifier(string)));
            }
        }, blockIngredient -> {
            var string = blockIngredient.value.map(entry -> Registry.BLOCK.getId(entry).toString(), tag -> "#" + tag.id());
            return new Dynamic<>(NbtOps.INSTANCE, NbtString.of(string));
        });
    public static BlockIngredient EMPTY = new BlockIngredient((Block) null);

    public BlockIngredient(Block value) {
        super(value);
    }

    public BlockIngredient(TagKey<Block> value) {
        super(value);
    }

    public BlockIngredient(Either<Block, TagKey<Block>> value) {
        super(value);
    }

    @Override
    public Registry<Block> getRegistry() {
        return Registry.BLOCK;
    }

    public boolean test(BlockItem block) {
        return test(block.getBlock());
    }

    public ItemStack getDisplayStack() {
        return value.map(Function.identity(), tag -> {
            var iterator = Registry.BLOCK.iterateEntries(tag).iterator();
            return iterator.hasNext() ? iterator.next().value() : Items.BARRIER;
        }).asItem().getDefaultStack();
    }

}
