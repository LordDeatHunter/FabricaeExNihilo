package wraith.fabricaeexnihilo.recipe.witchwater;

import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.ModRecipes;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.recipe.util.WeightedList;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.Optional;

public class WitchWaterWorldRecipe extends BaseRecipe<WitchWaterWorldRecipe.Context> {
    private final FluidIngredient target;
    private final WeightedList result;
    
    public WitchWaterWorldRecipe(Identifier id, FluidIngredient target, WeightedList result) {
        super(id);
        this.target = target;
        this.result = result;
    }
    
    public static Optional<WitchWaterWorldRecipe> find(Fluid fluid, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.WITCH_WATER_WORLD, new Context(fluid), world);
    }
    
    @Override
    public boolean matches(Context context, World world) {
        return target.test(context.fluid);
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WITCH_WATER_WORLD_SERIALIZER;
    }
    
    @Override
    public RecipeType<?> getType() {
        return ModRecipes.WITCH_WATER_WORLD;
    }
    
    @Override
    public ItemStack getDisplayStack() {
        return result.asListOfStacks().get(0);
    }
    
    public FluidIngredient getTarget() {
        return target;
    }
    
    public WeightedList getResult() {
        return result;
    }
    
    protected static record Context(Fluid fluid) implements RecipeContext {
    }
    
    public static class Serializer implements RecipeSerializer<WitchWaterWorldRecipe> {
        @Override
        public WitchWaterWorldRecipe read(Identifier id, JsonObject json) {
            var target = CodecUtils.fromJson(FluidIngredient.CODEC, json.get("target"));
            var result = CodecUtils.fromJson(WeightedList.CODEC, json.get("result"));
            
            return new WitchWaterWorldRecipe(id, target, result);
        }
        
        @Override
        public WitchWaterWorldRecipe read(Identifier id, PacketByteBuf buf) {
            var target = CodecUtils.fromPacket(FluidIngredient.CODEC, buf);
            var result = CodecUtils.fromPacket(WeightedList.CODEC, buf);
            
            return new WitchWaterWorldRecipe(id, target, result);
        }
        
        @Override
        public void write(PacketByteBuf buf, WitchWaterWorldRecipe recipe) {
            CodecUtils.toPacket(FluidIngredient.CODEC, recipe.target, buf);
            CodecUtils.toPacket(WeightedList.CODEC, recipe.result, buf);
        }
    }
}
