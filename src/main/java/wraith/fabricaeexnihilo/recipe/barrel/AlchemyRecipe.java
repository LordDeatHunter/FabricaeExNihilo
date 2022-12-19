package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.EmptyMode;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.recipe.util.EntityStack;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class AlchemyRecipe extends BaseRecipe<AlchemyRecipe.Context> {

    private final RegistryEntryList<Fluid> reactant;
    private final Ingredient catalyst;
    private final Loot byproduct;
    private final int delay;
    private final EntityStack toSpawn;
    private final BarrelMode result;

    public AlchemyRecipe(Identifier id, RegistryEntryList<Fluid> reactant, Ingredient catalyst, Loot byproduct, int delay, EntityStack toSpawn, BarrelMode result) {
        super(id);
        this.reactant = reactant;
        this.catalyst = catalyst;
        this.byproduct = byproduct;
        this.delay = delay;
        this.toSpawn = toSpawn;
        this.result = result;
    }


    public static Optional<AlchemyRecipe> find(FluidVariant reactant, ItemStack catalyst, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.ALCHEMY, new Context(reactant, catalyst), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return reactant.contains(context.reactant.getFluid().getRegistryEntry()) && catalyst.test(context.catalyst);
    }

    @Override
    public ItemStack getDisplayStack() {
        return byproduct.stack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ALCHEMY_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.ALCHEMY;
    }

    public RegistryEntryList<Fluid> getReactant() {
        return reactant;
    }

    public Ingredient getCatalyst() {
        return catalyst;
    }

    public Loot getByproduct() {
        return byproduct;
    }

    public int getDelay() {
        return delay;
    }

    public EntityStack getToSpawn() {
        return toSpawn;
    }

    public BarrelMode getResult() {
        return result;
    }

    public static class Serializer implements RecipeSerializer<AlchemyRecipe> {

        @Override
        public AlchemyRecipe read(Identifier id, JsonObject json) {
            var reactant = RegistryEntryLists.fromJson(Registries.FLUID.getKey(), json.get("reactant"));
            var catalyst = Ingredient.fromJson(json.get("catalyst"));
            var byproduct = json.has("byproduct") ? CodecUtils.fromJson(Loot.CODEC, json.get("byproduct")) : Loot.EMPTY;
            var delay = JsonHelper.getInt(json, "delay", 0);
            var toSpawn = json.has("toSpawn") ? CodecUtils.fromJson(EntityStack.CODEC, json.get("toSpawn")) : EntityStack.EMPTY;
            var result = json.has("result") ? CodecUtils.fromJson(BarrelMode.CODEC, json.get("result")) : new EmptyMode();

            return new AlchemyRecipe(id, reactant, catalyst, byproduct, delay, toSpawn, result);
        }

        @Override
        public AlchemyRecipe read(Identifier id, PacketByteBuf buf) {
            var reactant = RegistryEntryLists.fromPacket(Registries.FLUID.getKey(), buf);
            var catalyst = Ingredient.fromPacket(buf);
            var byproduct = CodecUtils.fromPacket(Loot.CODEC, buf);
            var delay = buf.readInt();
            var toSpawn = CodecUtils.fromPacket(EntityStack.CODEC, buf);
            var result = CodecUtils.fromPacket(BarrelMode.CODEC, buf);

            return new AlchemyRecipe(id, reactant, catalyst, byproduct, delay, toSpawn, result);
        }

        @Override
        public void write(PacketByteBuf buf, AlchemyRecipe recipe) {
            RegistryEntryLists.toPacket(Registries.FLUID.getKey(), recipe.reactant, buf);
            recipe.catalyst.write(buf);
            CodecUtils.toPacket(Loot.CODEC, recipe.byproduct, buf);
            buf.writeInt(recipe.delay);
            CodecUtils.toPacket(EntityStack.CODEC, recipe.toSpawn, buf);
            CodecUtils.toPacket(BarrelMode.CODEC, recipe.result, buf);
        }
    }

    protected record Context(FluidVariant reactant, ItemStack catalyst) implements RecipeContext {
    }
}
