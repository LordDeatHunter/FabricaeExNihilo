package wraith.fabricaeexnihilo.recipe.crucible;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.modules.ModRecipes;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class CrucibleRecipe extends BaseRecipe<CrucibleRecipe.Context> {
    private final ItemIngredient input;
    private final long amount;
    private final FluidVariant fluid;
    private final boolean isStone;
    
    public CrucibleRecipe(Identifier id, ItemIngredient input, long amount, FluidVariant fluid, boolean isStone) {
        super(id);
        this.input = input;
        this.amount = amount;
    
        this.fluid = fluid;
        this.isStone = isStone;
    }
    
    public static Optional<CrucibleRecipe> find(Item input, boolean isStone, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.CRUCIBLE, new Context(input, isStone), world);
    }
    
    @Override
    public boolean matches(Context context, World world) {
        return input.test(context.input) && isStone == context.isStone;
    }
    
    @Override
    public ItemStack getDisplayStack() {
        return fluid.getFluid().getBucketItem().getDefaultStack();
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CRUCIBLE_SERIALIZER;
    }
    
    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CRUCIBLE;
    }
    
    public ItemIngredient getInput() {
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
            var input = ItemIngredient.fromJson(json.get("input"));
            var amount = JsonHelper.getLong(json, "amount");
            var fluid = CodecUtils.fromJson(CodecUtils.FLUID_VARIANT, json.get("fluid"));
            var isStone = JsonHelper.getBoolean(json, "isStone");
            
            return new CrucibleRecipe(id, input, amount, fluid, isStone);
        }
    
        @Override
        public CrucibleRecipe read(Identifier id, PacketByteBuf buf) {
            var input = ItemIngredient.fromPacket(buf);
            var amount = buf.readLong();
            var fluid = CodecUtils.fromNbt(CodecUtils.FLUID_VARIANT, buf.readNbt());
            var isStone = buf.readBoolean();
            
            return new CrucibleRecipe(id, input, amount, fluid, isStone);
        }
    
        @Override
        public void write(PacketByteBuf buf, CrucibleRecipe recipe) {
            recipe.input.toPacket(buf);
            buf.writeLong(recipe.amount);
            buf.writeNbt((NbtCompound) CodecUtils.toNbt(CodecUtils.FLUID_VARIANT, recipe.fluid));
            buf.writeBoolean(recipe.isStone);
        }
    }
    
    protected static record Context(Item input, boolean isStone) implements RecipeContext { }
}
