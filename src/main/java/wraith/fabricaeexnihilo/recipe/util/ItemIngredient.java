package wraith.fabricaeexnihilo.recipe.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtString;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;

import java.util.List;

public class ItemIngredient extends AbstractIngredient<Item> {
    public static final Codec<ItemIngredient> CODEC = Codec.PASSTHROUGH
            .xmap(dynamic -> {
                var string = dynamic.asString().getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
                if (string.startsWith("#")) {
                    return new ItemIngredient(TagKey.of(Registry.ITEM_KEY, new Identifier(string.substring(1))));
                } else {
                    return new ItemIngredient(Registry.ITEM.get(new Identifier(string)));
                }
            }, itemIngredient -> {
                var string = itemIngredient.value.map(entry -> Registry.ITEM.getId(entry).toString(), tag -> "#" + tag.id());
                return new Dynamic<>(NbtOps.INSTANCE, NbtString.of(string));
            });
    
    public ItemIngredient(ItemConvertible value) {
        super(value == null ? null : value.asItem());
    }
    
    public ItemIngredient(TagKey<Item> value) {
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
    
    public static ItemIngredient EMPTY = new ItemIngredient((Item) null);
    
    @Override
    public Registry<Item> getRegistry() {
        return Registry.ITEM;
    }
}
