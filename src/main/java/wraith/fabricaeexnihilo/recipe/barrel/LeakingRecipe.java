package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class LeakingRecipe extends BaseRecipe<LeakingRecipe.Context> {
    private final BlockIngredient block;
    private final FluidIngredient fluid;
    private final long amount;
    private final Block result;

    public LeakingRecipe(Identifier id, BlockIngredient block, FluidIngredient fluid, long amount, Block result) {
        super(id);
        this.block = block;
        this.fluid = fluid;
        this.amount = amount;
        this.result = result;
    }

    public static Optional<LeakingRecipe> find(BlockState state, FluidVariant fluid, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.LEAKING, new Context(state, fluid), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return block.test(context.state) && fluid.test(context.fluid.getFluid());
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
        return result.asItem().getDefaultStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.LEAKING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.LEAKING;
    }

    public BlockIngredient getBlock() {
        return block;
    }

    public FluidIngredient getFluid() {
        return fluid;
    }

    public long getAmount() {
        return amount;
    }

    public Block getResult() {
        return result;
    }

    public static class Serializer implements RecipeSerializer<LeakingRecipe> {

        @Override
        public LeakingRecipe read(Identifier id, JsonObject json) {
            var block = BlockIngredient.fromJson(json.get("block"));
            var fluid = FluidIngredient.fromJson(json.get("fluid"));
            var amount = json.get("amount").getAsLong();
            var result = Registries.BLOCK.get(new Identifier(json.get("result").getAsString()));

            return new LeakingRecipe(id, block, fluid, amount, result);
        }

        @Override
        public LeakingRecipe read(Identifier id, PacketByteBuf buf) {
            var block = BlockIngredient.fromPacket(buf);
            var fluid = FluidIngredient.fromPacket(buf);
            var amount = buf.readLong();
            var result = Registries.BLOCK.get(buf.readIdentifier());

            return new LeakingRecipe(id, block, fluid, amount, result);
        }

        @Override
        public void write(PacketByteBuf buf, LeakingRecipe recipe) {
            recipe.block.toPacket(buf);
            recipe.fluid.toPacket(buf);
            buf.writeLong(recipe.amount);
            buf.writeIdentifier(Registries.BLOCK.getId(recipe.result));
        }
    }

    protected record Context(BlockState state, FluidVariant fluid) implements RecipeContext {
    }
}
