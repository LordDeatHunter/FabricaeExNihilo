package wraith.fabricaeexnihilo.api.crafting;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.util.ItemUtils;
import wraith.fabricaeexnihilo.util.RegistryUtils;

import java.util.*;

public class EntityTypeIngredient extends AbstractIngredient<EntityType<?>> {

    public EntityTypeIngredient(Collection<Tag.Identified<EntityType<?>>> tags, Set<EntityType<?>> matches) {
        super(tags, matches);
    }

    @SafeVarargs
    public EntityTypeIngredient(Tag.Identified<EntityType<?>>... tags) {
        this(Arrays.asList(tags), new HashSet<>());
    }

    public EntityTypeIngredient(EntityType<?>... matches) {
        this(new ArrayList<>(), new HashSet<>(Arrays.asList(matches)));
    }

    public EntityTypeIngredient() {
        this(new ArrayList<>(), new HashSet<>());
    }

    public boolean test(Entity entity) {
        return test(entity.getType());
    }

    public List<ItemStack> flattenListOfEggStacks() {
        return flatten(SpawnEggItem::forEntity).stream().filter(Objects::nonNull).map(ItemUtils::asStack).toList();
    }

    public List<EntryIngredient> asREIEntries() {
        return flattenListOfEggStacks().stream().map(EntryIngredients::of).toList();
    }

    @Override
    public JsonElement serializeElement(EntityType<?> entityType, JsonSerializationContext context) {
        return new JsonPrimitive(RegistryUtils.getId(entityType).toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EntityTypeIngredient other) {
            return this.tags.size() == other.tags.size() &&
                    this.matches.size() == other.matches.size() &&
                    this.tags.containsAll(other.tags) &&
                    this.matches.containsAll(other.matches);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return tags.hashCode() ^ matches.hashCode();
    }

    public static EntityTypeIngredient EMPTY = new EntityTypeIngredient();

    public static EntityTypeIngredient fromJson(JsonElement json, JsonDeserializationContext context) {
        return fromJson(json, context, val -> deserializeTag(val, context), val -> deserializeMatch(val, context), EntityTypeIngredient::new);
    }

    public static Tag.Identified<EntityType<?>> deserializeTag(JsonElement json, JsonDeserializationContext context) {
        return TagFactory.ENTITY_TYPE.create(new Identifier(json.getAsString().substring(1)));
    }

    public static EntityType<?> deserializeMatch(JsonElement json, JsonDeserializationContext context) {
        return Registry.ENTITY_TYPE.get(new Identifier(json.getAsString()));
    }

}