package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class FluidCombinationRecipe extends BaseRecipe<FluidCombinationRecipe.Context> {
    private final FluidIngredient contained;
    private final FluidIngredient other;
    private final BarrelMode result;

    public FluidCombinationRecipe(Identifier id, FluidIngredient contained, FluidIngredient other, BarrelMode result) {
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
        return contained.test(context.contained.getFluid()) && other.test(context.other.getFluid());
    }

    @Override
    public ItemStack craft(Context inventory, DynamicRegistryManager registryManager) {
        return null;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return null;
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

    public FluidIngredient getContained() {
        return contained;
    }

    public FluidIngredient getOther() {
        return other;
    }

    public static class Serializer implements RecipeSerializer<FluidCombinationRecipe> {

        @Override
        public FluidCombinationRecipe read(Identifier id, JsonObject json) {
            var contained = FluidIngredient.fromJson(json.get("contained"));
            var other = FluidIngredient.fromJson(json.get("other"));
            BarrelMode result = CodecUtils.fromJson(BarrelMode.CODEC, json.get("result"));

            return new FluidCombinationRecipe(id, contained, other, result);
        }

        @Override
        public FluidCombinationRecipe read(Identifier id, PacketByteBuf buf) {
            var contained = FluidIngredient.fromPacket(buf);
            var other = FluidIngredient.fromPacket(buf);
            BarrelMode result = CodecUtils.fromPacket(BarrelMode.CODEC, buf);

            return new FluidCombinationRecipe(id, contained, other, result);
        }

        @Override
        public void write(PacketByteBuf buf, FluidCombinationRecipe recipe) {
            recipe.contained.toPacket(buf);
            recipe.other.toPacket(buf);
            CodecUtils.toPacket(BarrelMode.CODEC, recipe.result, buf);
        }
    }

    protected record Context(FluidVariant contained, FluidVariant other) implements RecipeContext {
    }
}
