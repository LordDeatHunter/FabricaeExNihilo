package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class FluidTransformationRecipe extends BaseRecipe<FluidTransformationRecipe.Context> {
    private final FluidIngredient fluid;
    private final BlockIngredient catalyst;
    private final BarrelMode result;

    public FluidTransformationRecipe(Identifier id, FluidIngredient fluid, BlockIngredient catalyst, BarrelMode result) {
        super(id);
        this.fluid = fluid;
        this.catalyst = catalyst;
        this.result = result;
    }

    public static Optional<FluidTransformationRecipe> find(FluidVariant contained, BlockState catalyst, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.FLUID_TRANSFORMATION, new Context(contained, catalyst), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return fluid.test(context.contained.getFluid()) && catalyst.test(context.catalyst);
    }

    @Override
    public ItemStack getDisplayStack() {
        // FIXME: should use the result somehow
        return result.getReiResult().stream().map(EntryStack::cheatsAs).map(EntryStack::getValue).findFirst().orElse(ItemStack.EMPTY);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FLUID_TRANSFORMATION_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FLUID_TRANSFORMATION;
    }

    public BarrelMode getResult() {
        return result;
    }

    public FluidIngredient getFluid() {
        return fluid;
    }

    public BlockIngredient getCatalyst() {
        return catalyst;
    }

    public static class Serializer implements RecipeSerializer<FluidTransformationRecipe> {

        @Override
        public FluidTransformationRecipe read(Identifier id, JsonObject json) {
            var fluid = FluidIngredient.fromJson(json.get("fluid"));
            var catalyst = BlockIngredient.fromJson(json.get("catalyst"));
            var result = CodecUtils.fromJson(BarrelMode.CODEC, json.get("result"));

            return new FluidTransformationRecipe(id, fluid, catalyst, result);
        }

        @Override
        public FluidTransformationRecipe read(Identifier id, PacketByteBuf buf) {
            var fluid = FluidIngredient.fromPacket(buf);
            var catalyst = BlockIngredient.fromPacket(buf);
            var result = CodecUtils.fromPacket(BarrelMode.CODEC, buf);

            return new FluidTransformationRecipe(id, fluid, catalyst, result);
        }

        @Override
        public void write(PacketByteBuf buf, FluidTransformationRecipe recipe) {
            recipe.fluid.toPacket(buf);
            recipe.catalyst.toPacket(buf);
            CodecUtils.toPacket(BarrelMode.CODEC, recipe.result, buf);
        }
    }

    protected record Context(FluidVariant contained, BlockState catalyst) implements RecipeContext {
    }
}
