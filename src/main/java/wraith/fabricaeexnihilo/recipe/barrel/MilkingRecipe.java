package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class MilkingRecipe extends BaseRecipe<MilkingRecipe.Context> {

    private final RegistryEntryList<EntityType<?>> entity;
    private final FluidVariant fluid;
    private final long amount;
    private final int cooldown;

    public MilkingRecipe(Identifier id, RegistryEntryList<EntityType<?>> entity, FluidVariant fluid, long amount, int cooldown) {
        super(id);
        this.entity = entity;
        this.fluid = fluid;
        this.amount = amount;
        this.cooldown = cooldown;
    }

    public static Optional<MilkingRecipe> find(EntityType<?> entity, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.MILKING, new Context(entity), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return this.entity.contains(context.entity.getRegistryEntry());
    }

    @Override
    public ItemStack getDisplayStack() {
        return fluid.getFluid().getBucketItem().getDefaultStack();
    }

    public FluidVariant getFluid() {
        return fluid;
    }

    public long getAmount() {
        return amount;
    }

    public int getCooldown() {
        return cooldown;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.MILKING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.MILKING;
    }

    public RegistryEntryList<EntityType<?>> getEntity() {
        return entity;
    }

    public static class Serializer implements RecipeSerializer<MilkingRecipe> {

        @Override
        public MilkingRecipe read(Identifier id, JsonObject json) {
            var entity = RegistryEntryLists.fromJson(Registries.ENTITY_TYPE.getKey(), json.get("entity"));
            var fluid = CodecUtils.fromJson(CodecUtils.FLUID_VARIANT, json.get("fluid"));
            var amount = json.get("amount").getAsLong();
            var cooldown = json.get("cooldown").getAsInt();

            return new MilkingRecipe(id, entity, fluid, amount, cooldown);
        }

        @Override
        public MilkingRecipe read(Identifier id, PacketByteBuf buf) {
            var entity = RegistryEntryLists.fromPacket(Registries.ENTITY_TYPE.getKey(), buf);
            var fluid = CodecUtils.fromPacket(CodecUtils.FLUID_VARIANT, buf);
            var amount = buf.readLong();
            var cooldown = buf.readInt();

            return new MilkingRecipe(id, entity, fluid, amount, cooldown);
        }

        @Override
        public void write(PacketByteBuf buf, MilkingRecipe recipe) {
            RegistryEntryLists.toPacket(Registries.ENTITY_TYPE.getKey(), recipe.entity, buf);
            CodecUtils.toPacket(CodecUtils.FLUID_VARIANT, recipe.fluid, buf);
            buf.writeLong(recipe.amount);
            buf.writeInt(recipe.cooldown);
        }
    }

    protected record Context(EntityType<?> entity) implements RecipeContext {
    }
}
