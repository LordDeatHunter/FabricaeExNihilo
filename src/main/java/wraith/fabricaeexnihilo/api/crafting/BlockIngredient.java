package wraith.fabricaeexnihilo.api.crafting;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;

import java.util.*;

public class BlockIngredient extends AbstractIngredient<Block> {
    public static final Codec<BlockIngredient> CODEC = Codec.PASSTHROUGH
            .xmap(dynamic -> {
                var string = dynamic.asString().getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
                if (string.startsWith("#")) {
                    return new BlockIngredient(TagFactory.BLOCK.create(new Identifier(string.substring(1))));
                } else {
                    return new BlockIngredient(Registry.BLOCK.get(new Identifier(string)));
                }
            }, blockIngredient -> {
                var string = blockIngredient.value.map(entry -> Registry.BLOCK.getId(entry).toString(), tag -> "#" + ServerTagManagerHolder.getTagManager().getOrCreateTagGroup(Registry.BLOCK_KEY).getUncheckedTagId(tag));
                return new Dynamic<>(NbtOps.INSTANCE, NbtString.of(string));
            });
    
    public BlockIngredient(Block value) {
        super(value);
    }
    
    public BlockIngredient(Tag<Block> value) {
        super(value);
    }
    
    public BlockIngredient(Either<Block, Tag<Block>> value) {
        super(value);
    }
    
    public boolean test(BlockItem block) {
        return test(block.getBlock());
    }

    public List<EntryIngredient> asREIEntries() {
        return flatten(EntryIngredients::of);
    }
    
    public static BlockIngredient EMPTY = new BlockIngredient((Block)null);
    
    @Override
    public JsonElement toJson() {
        return CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
    }
    
    @Override
    public void toPacket(PacketByteBuf buf) {
        // Should be safe to cast here
        buf.writeNbt((NbtCompound) CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow(false, FabricaeExNihilo.LOGGER::warn));
    }
    
    public static BlockIngredient fromPacket(PacketByteBuf buf) {
        return CODEC.parse(NbtOps.INSTANCE, buf.readNbt()).getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
    }
    
    public static BlockIngredient fromJson(JsonElement json) {
        return CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
    }
}
