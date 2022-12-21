package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.Color;

import java.util.Optional;

public class CompostRecipe extends BaseRecipe<CompostRecipe.Context> {
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

    public static Optional<CompostRecipe> find(ItemStack input, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.COMPOST, new Context(input), world);
    }

    public Ingredient getInput() {
        return input;
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
            var result = CodecUtils.fromJson(CodecUtils.ITEM_STACK, json.get("result"));
            var input = Ingredient.fromJson(json.get("input"));
            var increment = JsonHelper.getDouble(json, "increment");
            var color = Color.fromJson(json.get("color"));

            return new CompostRecipe(id, result, input, increment, color);
        }

        @Override
        public CompostRecipe read(Identifier id, PacketByteBuf buf) {
            var result = buf.readItemStack();
            var input = Ingredient.fromPacket(buf);
            var increment = buf.readDouble();
            var color = new Color(buf.readInt());

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

    protected record Context(ItemStack input) implements RecipeContext {
    }
}
