package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
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
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class LeakingRecipe extends BaseRecipe<LeakingRecipe.Context> {

    private final RegistryEntryList<Block> block;
    private final RegistryEntryList<Fluid> fluid;
    private final long amount;
    private final Block result;

    public LeakingRecipe(Identifier id, RegistryEntryList<Block> block, RegistryEntryList<Fluid> fluid, long amount, Block result) {
        super(id);
        this.block = block;
        this.fluid = fluid;
        this.amount = amount;
        this.result = result;
    }

    public static Optional<LeakingRecipe> find(Block block, FluidVariant fluid, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.LEAKING, new Context(block, fluid), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return block.contains(context.block.getRegistryEntry()) && fluid.contains(context.fluid.getFluid().getRegistryEntry());
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

    public RegistryEntryList<Block> getBlock() {
        return block;
    }

    public RegistryEntryList<Fluid> getFluid() {
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
            var block = RegistryEntryLists.fromJson(Registries.BLOCK.getKey(), json.get("block"));
            var fluid = RegistryEntryLists.fromJson(Registries.FLUID.getKey(), json.get("fluid"));
            var amount = json.get("amount").getAsLong();
            var result = Registries.BLOCK.get(new Identifier(json.get("result").getAsString()));

            return new LeakingRecipe(id, block, fluid, amount, result);
        }

        @Override
        public LeakingRecipe read(Identifier id, PacketByteBuf buf) {
            var block = RegistryEntryLists.fromPacket(Registries.BLOCK.getKey(), buf);
            var fluid = RegistryEntryLists.fromPacket(Registries.FLUID.getKey(), buf);
            var amount = buf.readLong();
            var result = Registries.BLOCK.get(buf.readIdentifier());

            return new LeakingRecipe(id, block, fluid, amount, result);
        }

        @Override
        public void write(PacketByteBuf buf, LeakingRecipe recipe) {
            RegistryEntryLists.toPacket(Registries.BLOCK.getKey(), recipe.block, buf);
            RegistryEntryLists.toPacket(Registries.FLUID.getKey(), recipe.fluid, buf);
            buf.writeLong(recipe.amount);
            buf.writeIdentifier(Registries.BLOCK.getId(recipe.result));
        }
    }

    protected record Context(Block block, FluidVariant fluid) implements RecipeContext {
    }
}
