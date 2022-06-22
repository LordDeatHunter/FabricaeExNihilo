package wraith.fabricaeexnihilo.recipe.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtString;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class EntityTypeIngredient extends AbstractIngredient<EntityType<?>> {

    public static final Codec<EntityTypeIngredient> CODEC = Codec.PASSTHROUGH
        .xmap(dynamic -> {
            var string = dynamic.asString().getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
            if (string.startsWith("#")) {
                return new EntityTypeIngredient(TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(string.substring(1))));
            } else {
                return new EntityTypeIngredient(Registry.ENTITY_TYPE.get(new Identifier(string)));
            }
        }, itemIngredient -> {
            var string = itemIngredient.value.map(entry -> Registry.ENTITY_TYPE.getId(entry).toString(), tag -> "#" + tag.id());
            return new Dynamic<>(NbtOps.INSTANCE, NbtString.of(string));
        });
    public static EntityTypeIngredient EMPTY = new EntityTypeIngredient((EntityType<?>) null);

    public EntityTypeIngredient(EntityType<?> value) {
        super(value);
    }

    public EntityTypeIngredient(TagKey<EntityType<?>> value) {
        super(value);
    }

    public EntityTypeIngredient(Either<EntityType<?>, TagKey<EntityType<?>>> value) {
        super(value);
    }

    @Override
    public Registry<EntityType<?>> getRegistry() {
        return Registry.ENTITY_TYPE;
    }

    public boolean test(Entity entity) {
        return test(entity.getType());
    }

    public List<ItemStack> flattenListOfEggStacks() {
        return streamEntries().map(SpawnEggItem::forEntity).toList().stream().filter(Objects::nonNull).map(ItemUtils::asStack).toList();
    }

    public List<EntityType<?>> flattenListOfEntities() {
        return streamEntries().toList().stream().filter(Objects::nonNull).toList();
    }

    public <U> List<U> flattenListOfEggStacks(Function<ItemStack, U> func) {
        return flattenListOfEggStacks().stream().map(func).toList();
    }

}