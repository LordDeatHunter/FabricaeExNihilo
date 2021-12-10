package wraith.fabricaeexnihilo.api.crafting;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import wraith.fabricaeexnihilo.util.RegistryUtils;

import java.util.*;
import java.util.stream.Collectors;

public class BlockIngredient extends AbstractIngredient<Block> {

    public BlockIngredient(Collection<Tag.Identified<Block>> tags, Set<Block> matches) {
        super(tags, matches);
    }

    public BlockIngredient(Block... matches) {
        this(new ArrayList<>(), new HashSet<>(Arrays.asList(matches)));
    }

    public BlockIngredient(BlockItem... matches) {
        this(new ArrayList<>(), Arrays.stream(matches).map(BlockItem::getBlock).collect(Collectors.toSet()));
    }

    @SafeVarargs
    public BlockIngredient(Tag.Identified<Block>... tags) {
        this(Arrays.asList(tags), new HashSet<>());
    }

    public BlockIngredient() {
        this(new ArrayList<>(), new HashSet<>());
    }

    @Override
    public @NotNull JsonElement serializeElement(Block block, @NotNull JsonSerializationContext context) {
        return new JsonPrimitive(RegistryUtils.getId(block).toString());
    }

    public boolean test(BlockItem block) {
        return test(block.getBlock());
    }

    public List<EntryIngredient> asREIEntries() {
        return flatten(EntryIngredients::of);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BlockIngredient other) {
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

    public static BlockIngredient EMPTY = new BlockIngredient();

    public static BlockIngredient fromJson(JsonElement json, JsonDeserializationContext context) {
        return fromJson(json, context, val -> deserializeTag(val, context), val -> deserializeMatch(val, context), BlockIngredient::new);
    }

    public static Tag.Identified<Block> deserializeTag(JsonElement json, JsonDeserializationContext context) {
        return TagFactory.BLOCK.create(new Identifier(json.getAsString().substring(1)));
    }

    public static Block deserializeMatch(JsonElement json, JsonDeserializationContext context) {
        return Registry.BLOCK.get(new Identifier(json.getAsString()));
    }

}
