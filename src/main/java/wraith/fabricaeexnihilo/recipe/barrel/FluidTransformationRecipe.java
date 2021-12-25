package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.crafting.BlockIngredient;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.modules.ModRecipes;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.RecipeContext;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class FluidTransformationRecipe extends BaseRecipe<FluidTransformationRecipe.FluidTransformationRecipeContext> {
    private final FluidIngredient contained;
    private final BlockIngredient catalyst;
    private final BarrelMode result;
    
    public FluidTransformationRecipe(Identifier id, FluidIngredient contained, BlockIngredient catalyst, BarrelMode result) {
        super(id);
        this.contained = contained;
        this.catalyst = catalyst;
        this.result = result;
    }
    
    public static Optional<FluidTransformationRecipe> find(FluidVariant contained, Block catalyst, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.FLUID_TRANSFORMATION, new FluidTransformationRecipeContext(contained, catalyst), world);
    }
    
    @Override
    public boolean matches(FluidTransformationRecipeContext context, World world) {
        return contained.test(context.contained) && catalyst.test(context.catalyst);
    }
    
    @Override
    public ItemStack getDisplayStack() {
        // FIXME: there might not be any buckets, should use the result somehow...
        return contained.flattenListOfBuckets().get(0);
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
    
    public FluidIngredient getContained() {
        return contained;
    }
    
    public BlockIngredient getCatalyst() {
        return catalyst;
    }
    
    public static class Serializer implements RecipeSerializer<FluidTransformationRecipe> {
        @Override
        public FluidTransformationRecipe read(Identifier id, JsonObject json) {
            FluidIngredient contained = FluidIngredient.fromJson(json.get("contained"));
            BlockIngredient catalyst = BlockIngredient.fromJson(json.get("catalyst"));
            BarrelMode result = BarrelMode.fromJson(json.get("result"));
            
            return new FluidTransformationRecipe(id, contained, catalyst, result);
        }
        
        @Override
        public FluidTransformationRecipe read(Identifier id, PacketByteBuf buf) {
            FluidIngredient contained = FluidIngredient.fromPacket(buf);
            BlockIngredient catalyst = BlockIngredient.fromPacket(buf);
            BarrelMode result = BarrelMode.fromPacket(buf);
            
            return new FluidTransformationRecipe(id, contained, catalyst, result);
        }
        
        @Override
        public void write(PacketByteBuf buf, FluidTransformationRecipe recipe) {
            recipe.contained.toPacket(buf);
            recipe.catalyst.toPacket(buf);
            recipe.result.toPacket(buf);
        }
    }
    
    protected static record FluidTransformationRecipeContext(FluidVariant contained, Block catalyst) implements RecipeContext { }
}
