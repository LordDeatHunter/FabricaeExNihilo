package wraith.fabricaeexnihilo.api.crafting;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import wraith.fabricaeexnihilo.util.RegistryUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ItemIngredient extends AbstractIngredient<Item> {

    public ItemIngredient(Collection<Tag.Identified<Item>> tags, Set<Item> matches) {
        super(tags, matches);
    }

    public ItemIngredient(Item... matches) {
        this(new ArrayList<>(), new HashSet<>(Arrays.asList(matches)));
    }

    public ItemIngredient(ItemStack... matches) {
        this(new ArrayList<>(), Arrays.stream(matches).map(ItemStack::getItem).collect(Collectors.toSet()));
    }

    public ItemIngredient(ItemConvertible... matches) {
        this(new ArrayList<>(), Arrays.stream(matches).map(ItemConvertible::asItem).collect(Collectors.toSet()));
    }

    @SafeVarargs
    public ItemIngredient(Tag.Identified<Item>... tags) {
        this(Arrays.asList(tags), new HashSet<>());
    }

    public ItemIngredient() {
        this(new ArrayList<>(), new HashSet<>());
    }

    @Override
    public @NotNull JsonElement serializeElement(Item item, @NotNull JsonSerializationContext context) {
        return new JsonPrimitive(RegistryUtils.getId(item).toString());
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemIngredient other) {
            return this.tags.size() == other.tags.size() &&
                    this.matches.size() == other.matches.size() &&
                    this.tags.containsAll(other.tags) &&
                    this.matches.containsAll(other.matches);
        }
        return false;
    }

    public int hashCode() {
        return tags.hashCode() ^ matches.hashCode();
    }

    public static ItemIngredient EMPTY = new ItemIngredient();

    public static ItemIngredient fromJson(JsonElement json, JsonDeserializationContext context) {
        return fromJson(json, context, val -> deserializeTag(val, context), val -> deserializeMatch(val, context), ItemIngredient::new);
    }

    public static Tag.Identified<Item> deserializeTag(JsonElement json, JsonDeserializationContext context) {
        return TagFactory.ITEM.create(new Identifier(json.getAsString().substring(1)));
    }

    public static Item deserializeMatch(JsonElement json, JsonDeserializationContext context) {
        return Registry.ITEM.get(new Identifier(json.getAsString()));
    }

}
