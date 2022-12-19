package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Block;
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
public class FluidTransformationRecipe extends BaseRecipe<FluidTransformationRecipe.Context> {

    private final RegistryEntryList<Fluid> fluid;
    private final RegistryEntryList<Block> catalyst;
    private final BarrelMode result;

    public FluidTransformationRecipe(Identifier id, RegistryEntryList<Fluid> fluid, RegistryEntryList<Block> catalyst, BarrelMode result) {
        super(id);
        this.fluid = fluid;
        this.catalyst = catalyst;
        this.result = result;
    }

    public static Optional<FluidTransformationRecipe> find(FluidVariant contained, Block catalyst, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.FLUID_TRANSFORMATION, new Context(contained, catalyst), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return fluid.contains(context.contained.getFluid().getRegistryEntry()) && catalyst.contains(context.catalyst.getRegistryEntry());
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

    public RegistryEntryList<Fluid> getFluid() {
        return fluid;
    }

    public RegistryEntryList<Block> getCatalyst() {
        return catalyst;
    }

    public static class Serializer implements RecipeSerializer<FluidTransformationRecipe> {

        @Override
        public FluidTransformationRecipe read(Identifier id, JsonObject json) {
            var fluid = RegistryEntryLists.fromJson(Registries.FLUID.getKey(), json.get("fluid"));
            var catalyst = RegistryEntryLists.fromJson(Registries.BLOCK.getKey(), json.get("catalyst"));
            var result = CodecUtils.fromJson(BarrelMode.CODEC, json.get("result"));

            return new FluidTransformationRecipe(id, fluid, catalyst, result);
        }

        @Override
        public FluidTransformationRecipe read(Identifier id, PacketByteBuf buf) {
            var fluid = RegistryEntryLists.fromPacket(Registries.FLUID.getKey(), buf);
            var catalyst = RegistryEntryLists.fromPacket(Registries.BLOCK.getKey(), buf);
            var result = CodecUtils.fromPacket(BarrelMode.CODEC, buf);

            return new FluidTransformationRecipe(id, fluid, catalyst, result);
        }

        @Override
        public void write(PacketByteBuf buf, FluidTransformationRecipe recipe) {
            RegistryEntryLists.toPacket(Registries.FLUID.getKey(), recipe.fluid, buf);
            RegistryEntryLists.toPacket(Registries.BLOCK.getKey(), recipe.catalyst, buf);
            CodecUtils.toPacket(BarrelMode.CODEC, recipe.result, buf);
        }
    }

    protected record Context(FluidVariant contained, Block catalyst) implements RecipeContext {
    }
}
