package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
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
import wraith.fabricaeexnihilo.util.Color;

import java.util.Optional;

public class CompostRecipe extends BaseRecipe<CompostRecipe.Context> {
    private final ItemStack result;
    private final ItemIngredient input;
    private final double increment;
    private final Color color;
    
    public CompostRecipe(Identifier id, ItemStack result, ItemIngredient input, double increment, Color color) {
        super(id);
        this.result = result;
        this.input = input;
        this.increment = increment;
        this.color = color;
    }
    
    public static Optional<CompostRecipe> find(ItemStack input, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.COMPOST, new Context(input), world);
    }
    
    public double getIncrement() {
        return increment;
    }
    
    public Color getColor() {
        return color;
    }
    
    public ItemStack getResult() {
        return result;
    }
    
    @Override
    public boolean matches(Context context, World world) {
        return input.test(context.input);
    }
    
    @Override
    public ItemStack getDisplayStack() {
        return result;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.COMPOST_SERIALIZER;
    }
    
    @Override
    public RecipeType<?> getType() {
        return ModRecipes.COMPOST;
    }
    
    public static class Serializer implements RecipeSerializer<CompostRecipe> {
        @Override
        public CompostRecipe read(Identifier id, JsonObject json) {
            ItemStack result = JsonHelper.getItem(json, "result").getDefaultStack();
            ItemIngredient input = ItemIngredient.fromJson(json.get("input"));
            double increment = JsonHelper.getDouble(json, "increment");
            Color color = Color.fromJson(json.get("color"));
            
            return new CompostRecipe(id, result, input, increment, color);
        }
    
        @Override
        public CompostRecipe read(Identifier id, PacketByteBuf buf) {
            ItemStack result = buf.readItemStack();
            ItemIngredient input = ItemIngredient.fromPacket(buf);
            double increment = buf.readDouble();
            Color color = new Color(buf.readInt());
    
            return new CompostRecipe(id, result, input, increment, color);
        }
    
        @Override
        public void write(PacketByteBuf buf, CompostRecipe recipe) {
            buf.writeItemStack(recipe.result);
            recipe.input.toPacket(buf);
            buf.writeDouble(recipe.increment);
            buf.writeInt(recipe.color.toInt());
        }
    }
    
    protected static record Context(ItemStack input) implements RecipeContext { }
}
