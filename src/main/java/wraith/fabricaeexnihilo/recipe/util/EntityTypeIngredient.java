package wraith.fabricaeexnihilo.recipe.util;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.List;
import java.util.Objects;

public class EntityTypeIngredient extends AbstractIngredient<EntityType<?>> {
    public static final Codec<EntityTypeIngredient> CODEC = Codec.PASSTHROUGH
            .xmap(dynamic -> {
                var string = dynamic.asString().getOrThrow(false, FabricaeExNihilo.LOGGER::warn);
                if (string.startsWith("#")) {
                    return new EntityTypeIngredient(TagFactory.ENTITY_TYPE.create(new Identifier(string.substring(1))));
                } else {
                    return new EntityTypeIngredient(Registry.ENTITY_TYPE.get(new Identifier(string)));
                }
            }, itemIngredient -> {
                var string = itemIngredient.value.map(entry -> Registry.ENTITY_TYPE.getId(entry).toString(),
                        tag -> "#" + ServerTagManagerHolder.getTagManager()
                                .getOrCreateTagGroup(Registry.ENTITY_TYPE_KEY)
                                .getUncheckedTagId(tag));
                return new Dynamic<>(NbtOps.INSTANCE, NbtString.of(string));
            });
    
    public EntityTypeIngredient(EntityType<?> value) {
        super(value);
    }
    
    public EntityTypeIngredient(Tag<EntityType<?>> value) {
        super(value);
    }
    
    public EntityTypeIngredient(Either<EntityType<?>, Tag<EntityType<?>>> value) {
        super(value);
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
    
    public static EntityTypeIngredient EMPTY = new EntityTypeIngredient((EntityType<?>) null);
}