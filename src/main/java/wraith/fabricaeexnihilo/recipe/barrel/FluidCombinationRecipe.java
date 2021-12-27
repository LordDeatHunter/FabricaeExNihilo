package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.modules.ModRecipes;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.RecipeContext;

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
        return contained.test(context.contained) && other.test(context.other);
    }
    
    @Override
    public ItemStack getDisplayStack() {
        // FIXME: there might not be any buckets, should use the result somehow...
        return other.flattenListOfBuckets().get(0);
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
            FluidIngredient contained = FluidIngredient.fromJson(json.get("contained"));
            FluidIngredient other = FluidIngredient.fromJson(json.get("other"));
            BarrelMode result = BarrelMode.fromJson(json.get("result"));
            
            return new FluidCombinationRecipe(id, contained, other, result);
        }
        
        @Override
        public FluidCombinationRecipe read(Identifier id, PacketByteBuf buf) {
            FluidIngredient contained = FluidIngredient.fromPacket(buf);
            FluidIngredient other = FluidIngredient.fromPacket(buf);
            BarrelMode result = BarrelMode.fromPacket(buf);
            
            return new FluidCombinationRecipe(id, contained, other, result);
        }
        
        @Override
        public void write(PacketByteBuf buf, FluidCombinationRecipe recipe) {
            recipe.contained.toPacket(buf);
            recipe.other.toPacket(buf);
            recipe.result.toPacket(buf);
        }
    }
    
    protected static record Context(FluidVariant contained, FluidVariant other) implements RecipeContext { }
}
