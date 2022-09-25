package wraith.fabricaeexnihilo.recipe.crucible;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class CrucibleRecipe extends BaseRecipe<CrucibleRecipe.Context> {
    private final Ingredient input;
    private final long amount;
    private final FluidVariant fluid;
    private final boolean requiresFireproofCrucible;
    
    public CrucibleRecipe(Identifier id, Ingredient input, long amount, FluidVariant fluid, boolean requiresFireproofCrucible) {
        super(id);
        this.input = input;
        this.amount = amount;
        this.fluid = fluid;
        this.requiresFireproofCrucible = requiresFireproofCrucible;
    }
    
    public static Optional<CrucibleRecipe> find(ItemStack input, boolean isFireproof, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.CRUCIBLE, new Context(input, isFireproof), world);
    }
    
    @Override
    public boolean matches(Context context, World world) {
        return input.test(context.input) && (!requiresFireproofCrucible || context.isFireproof);
    }
    
    @Override
    public ItemStack getDisplayStack() {
        return fluid.getFluid().getBucketItem().getDefaultStack();
    }
    
    public boolean requiresFireproofCrucible() {
        return requiresFireproofCrucible;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CRUCIBLE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CRUCIBLE;
    }

    public Ingredient getInput() {
        return input;
    }

    public long getAmount() {
        return amount;
    }

    public FluidVariant getFluid() {
        return fluid;
    }

    public static class Serializer implements RecipeSerializer<CrucibleRecipe> {
        @Override
        public CrucibleRecipe read(Identifier id, JsonObject json) {
            var input = JsonHelper.hasString(json, "input") ? Ingredient.ofItems(Registry.ITEM.get(new Identifier(json.get("input").getAsString()))) : Ingredient.fromJson(json.get("input"));
            var amount = JsonHelper.getLong(json, "amount");
            var fluid = CodecUtils.fromJson(CodecUtils.FLUID_VARIANT, json.get("fluid"));
            var isFireproof = JsonHelper.getBoolean(json, "requiresFireproofCrucible"); // TODO: rename json field
            

            return new CrucibleRecipe(id, input, amount, fluid, isFireproof);
        }

        @Override
        public CrucibleRecipe read(Identifier id, PacketByteBuf buf) {
            var input = Ingredient.fromPacket(buf);
            var amount = buf.readLong();
            var fluid = CodecUtils.fromPacket(CodecUtils.FLUID_VARIANT, buf);
            var isFireproof = buf.readBoolean();

            return new CrucibleRecipe(id, input, amount, fluid, isFireproof);
        }

        @Override
        public void write(PacketByteBuf buf, CrucibleRecipe recipe) {
            recipe.input.write(buf);
            buf.writeLong(recipe.amount);
            CodecUtils.toPacket(CodecUtils.FLUID_VARIANT, recipe.fluid, buf);
            buf.writeBoolean(recipe.requiresFireproofCrucible);
        }
    }

    protected record Context(ItemStack input, boolean isFireproof) implements RecipeContext {
    }
}
