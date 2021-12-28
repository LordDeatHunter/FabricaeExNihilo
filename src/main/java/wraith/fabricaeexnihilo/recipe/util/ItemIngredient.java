package wraith.fabricaeexnihilo.recipe.util;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;

import java.util.List;

public class ItemIngredient extends AbstractIngredient<Item> {
    public static final Codec<ItemIngredient> CODEC = Codec.PASSTHROUGH
            .xmap(dynamic -> {
                var string = dynamic.asString().getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
                if (string.startsWith("#")) {
                    return new ItemIngredient(TagFactory.ITEM.create(new Identifier(string.substring(1))));
                } else {
                    return new ItemIngredient(Registry.ITEM.get(new Identifier(string)));
                }
            }, itemIngredient -> {
                var string = itemIngredient.value.map(entry -> Registry.ITEM.getId(entry).toString(), tag -> "#" + ServerTagManagerHolder.getTagManager().getOrCreateTagGroup(Registry.ITEM_KEY).getUncheckedTagId(tag));
                return new Dynamic<>(NbtOps.INSTANCE, NbtString.of(string));
            });
    
    public ItemIngredient(ItemConvertible value) {
        super(value == null ? null : value.asItem());
    }
    
    public ItemIngredient(Tag<Item> value) {
        super(value);
    }
    
    public ItemIngredient(Either<Item, Tag<Item>> value) {
        super(value);
    }
    
    public ItemIngredient(ItemStack stack) {
        this(stack.getItem());
    }
    
    public boolean test(ItemStack stack) {
        return test(stack.getItem());
    }

    public boolean test(ItemConvertible item) {
        return test(item.asItem());
    }

    public List<EntryIngredient> asREIEntries() {
        return flatten(EntryIngredients::of);
    }
    
    public static ItemIngredient EMPTY = new ItemIngredient((Item)null);
    
    @Override
    public JsonElement toJson() {
        return CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
    }
    
    @Override
    public void toPacket(PacketByteBuf buf) {
        // Should be safe to cast here
        buf.writeNbt((NbtCompound) CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow(false, FabricaeExNihilo.LOGGER::warn));
    }
    
    public static ItemIngredient fromPacket(PacketByteBuf buf) {
        return CODEC.parse(NbtOps.INSTANCE, buf.readNbt()).getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
    }
    
    public static ItemIngredient fromJson(JsonElement json) {
        return CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
    }
}
