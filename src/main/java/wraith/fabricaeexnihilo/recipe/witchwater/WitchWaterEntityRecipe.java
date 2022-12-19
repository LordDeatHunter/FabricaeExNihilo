package wraith.fabricaeexnihilo.recipe.witchwater;

import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.Optional;

public class WitchWaterEntityRecipe extends BaseRecipe<WitchWaterEntityRecipe.Context> {

    private final RegistryEntryList<EntityType<?>> target;
    //TODO: nbt filter?
    private final @Nullable VillagerProfession profession;
    private final EntityType<?> result;

    public WitchWaterEntityRecipe(Identifier id, RegistryEntryList<EntityType<?>> target, @Nullable VillagerProfession profession, EntityType<?> result) {
        super(id);
        this.target = target;
        this.profession = profession;
        this.result = result;
    }

    public static Optional<WitchWaterEntityRecipe> find(Entity entity, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.WITCH_WATER_ENTITY, new Context(entity), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        if (!target.contains(context.entity.getType().getRegistryEntry()))
            return false;

        return !(context.entity instanceof VillagerEntity villager) || profession == null || villager.getVillagerData().getProfession() == profession;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WITCH_WATER_ENTITY_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.WITCH_WATER_ENTITY;
    }

    @Override
    public ItemStack getDisplayStack() {
        var egg = SpawnEggItem.forEntity(result);
        return egg == null ? ItemStack.EMPTY : egg.getDefaultStack();
    }

    public RegistryEntryList<EntityType<?>> getTarget() {
        return target;
    }

    public EntityType<?> getResult() {
        return result;
    }

    public @Nullable VillagerProfession getProfession() {
        return profession;
    }

    protected record Context(Entity entity) implements RecipeContext {}

    public static class Serializer implements RecipeSerializer<WitchWaterEntityRecipe> {

        @Override
        public WitchWaterEntityRecipe read(Identifier id, JsonObject json) {
            var target = RegistryEntryLists.fromJson(Registries.ENTITY_TYPE.getKey(), json.get("target"));
            var profession = json.has("profession") ? Registries.VILLAGER_PROFESSION.get(new Identifier(JsonHelper.getString(json, "profession"))) : null;
            var result = Registries.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(json, "result")));

            return new WitchWaterEntityRecipe(id, target, profession, result);
        }

        @Override
        public WitchWaterEntityRecipe read(Identifier id, PacketByteBuf buf) {
            var target = RegistryEntryLists.fromPacket(Registries.ENTITY_TYPE.getKey(), buf);
            var profession = buf.readBoolean() ? Registries.VILLAGER_PROFESSION.get(buf.readIdentifier()) : null;
            var result = Registries.ENTITY_TYPE.get(buf.readIdentifier());

            return new WitchWaterEntityRecipe(id, target, profession, result);
        }

        @Override
        public void write(PacketByteBuf buf, WitchWaterEntityRecipe recipe) {
            RegistryEntryLists.toPacket(Registries.ENTITY_TYPE.getKey(), recipe.target, buf);
            buf.writeBoolean(recipe.profession != null);
            if (recipe.profession != null) {
                buf.writeIdentifier(Registries.VILLAGER_PROFESSION.getId(recipe.profession));
            }
            buf.writeIdentifier(Registries.ENTITY_TYPE.getId(recipe.result));
        }
    }
}
