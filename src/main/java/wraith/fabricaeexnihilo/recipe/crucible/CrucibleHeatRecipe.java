package wraith.fabricaeexnihilo.recipe.crucible;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.crafting.BlockIngredient;
import wraith.fabricaeexnihilo.modules.ModRecipes;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.RecipeContext;

import java.util.Optional;

public class CrucibleHeatRecipe extends BaseRecipe<CrucibleHeatRecipe.CrucibleHeatRecipeRecipeContext> {
    private final BlockIngredient block;
    private final int heat;
    
    protected CrucibleHeatRecipe(Identifier id, BlockIngredient block, int heat) {
        super(id);
        this.block = block;
        this.heat = heat;
    }
    
    public static Optional<CrucibleHeatRecipe> find(Block block, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.CRUCIBLE_HEAT, new CrucibleHeatRecipeRecipeContext(block), world);
    }
    
    @Override
    public boolean matches(CrucibleHeatRecipeRecipeContext context, World world) {
        return block.test(context.block);
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CRUCIBLE_HEAT_SERIALIZER;
    }
    
    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CRUCIBLE_HEAT;
    }
    
    @Override
    public ItemStack getDisplayStack() {
        return block.getDisplayStack();
    }
    
    public BlockIngredient getBlock() {
        return block;
    }
    
    public int getHeat() {
        return heat;
    }
    
    protected static record CrucibleHeatRecipeRecipeContext(Block block) implements RecipeContext {}
    
    public static class Serializer implements RecipeSerializer<CrucibleHeatRecipe> {
        @Override
        public CrucibleHeatRecipe read(Identifier id, JsonObject json) {
            var block = BlockIngredient.fromJson(json.get("block"));
            var heat = JsonHelper.getInt(json, "heat");
            
            return new CrucibleHeatRecipe(id, block, heat);
        }
    
        @Override
        public CrucibleHeatRecipe read(Identifier id, PacketByteBuf buf) {
            var block = BlockIngredient.fromPacket(buf);
            var heat = buf.readInt();
    
            return new CrucibleHeatRecipe(id, block, heat);
        }
    
        @Override
        public void write(PacketByteBuf buf, CrucibleHeatRecipe recipe) {
            recipe.block.toPacket(buf);
            buf.writeInt(recipe.heat);
        }
    }
}
