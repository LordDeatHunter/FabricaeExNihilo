package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class FluidCombinationRecipe extends BaseRecipe<FluidCombinationRecipe.Context> {

    private final RegistryEntryList<Fluid> contained;
    private final RegistryEntryList<Fluid> other;
    private final BarrelMode result;

    public FluidCombinationRecipe(Identifier id, RegistryEntryList<Fluid> contained, RegistryEntryList<Fluid> other, BarrelMode result) {
        super(id);
        this.contained = contained;
        this.other = other;
        this.result = result;
    }

    public static Optional<FluidCombinationRecipe> find(FluidVariant contained, FluidVariant other, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.FLUID_COMBINATION, new Context(contained, other), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return contained.contains(context.contained.getFluid().getRegistryEntry()) && other.contains(context.other.getFluid().getRegistryEntry());
    }

    @Override
    public ItemStack getDisplayStack() {
        var reiResult = result.getReiResult();
        return reiResult.size() > 0 ? reiResult.get(0).cheatsAs().getValue() : ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FLUID_COMBINATION_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FLUID_COMBINATION;
    }

    public BarrelMode getResult() {
        return result;
    }

    public RegistryEntryList<Fluid> getContained() {
        return contained;
    }

    public RegistryEntryList<Fluid> getOther() {
        return other;
    }

    public static class Serializer implements RecipeSerializer<FluidCombinationRecipe> {

        @Override
        public FluidCombinationRecipe read(Identifier id, JsonObject json) {
            var contained = RegistryEntryLists.fromJson(Registries.FLUID.getKey(), json.get("contained"));
            var other = RegistryEntryLists.fromJson(Registries.FLUID.getKey(), json.get("other"));
            BarrelMode result = CodecUtils.fromJson(BarrelMode.CODEC, json.get("result"));

            return new FluidCombinationRecipe(id, contained, other, result);
        }

        @Override
        public FluidCombinationRecipe read(Identifier id, PacketByteBuf buf) {
            var contained = RegistryEntryLists.fromPacket(Registries.FLUID.getKey(), buf);
            var other = RegistryEntryLists.fromPacket(Registries.FLUID.getKey(), buf);
            BarrelMode result = CodecUtils.fromPacket(BarrelMode.CODEC, buf);

            return new FluidCombinationRecipe(id, contained, other, result);
        }

        @Override
        public void write(PacketByteBuf buf, FluidCombinationRecipe recipe) {
            RegistryEntryLists.toPacket(Registries.FLUID.getKey(), recipe.contained, buf);
            RegistryEntryLists.toPacket(Registries.FLUID.getKey(), recipe.other, buf);
            CodecUtils.toPacket(BarrelMode.CODEC, recipe.result, buf);
        }
    }

    protected record Context(FluidVariant contained, FluidVariant other) implements RecipeContext {
    }
}
