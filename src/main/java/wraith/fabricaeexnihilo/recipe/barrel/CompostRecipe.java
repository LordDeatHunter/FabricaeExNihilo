package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.modules.ModRecipes;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.util.Color;

public class CompostRecipe extends BaseRecipe<SimpleInventory> {
    private final ItemStack result;
    private final Ingredient input;
    private final double increment;
    private final Color color;
    
    public CompostRecipe(Identifier id, ItemStack result, Ingredient input, double increment, Color color) {
        super(id);
        this.result = result;
        this.input = input;
        this.increment = increment;
        this.color = color;
    }
    
    public double getIncrement() {
        return increment;
    }
    
    public Color getColor() {
        return color;
    }
    
    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        return input.test(inventory.getStack(0));
    }
    
    @Override
    public ItemStack getOutput() {
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
            Ingredient input = Ingredient.fromJson(JsonHelper.getObject(json, "input"));
            double increment = JsonHelper.getDouble(json, "increment");
            Color color = Color.fromJson(json.get("color"));
            
            return new CompostRecipe(id, result, input, increment, color);
        }
    
        @Override
        public CompostRecipe read(Identifier id, PacketByteBuf buf) {
            ItemStack result = buf.readItemStack();
            Ingredient input = Ingredient.fromPacket(buf);
            double increment = buf.readDouble();
            Color color = new Color(buf.readInt());
    
            return new CompostRecipe(id, result, input, increment, color);
        }
    
        @Override
        public void write(PacketByteBuf buf, CompostRecipe recipe) {
            buf.writeItemStack(recipe.result);
            recipe.input.write(buf);
            buf.writeDouble(recipe.increment);
            buf.writeInt(recipe.color.toInt());
        }
    }
}
