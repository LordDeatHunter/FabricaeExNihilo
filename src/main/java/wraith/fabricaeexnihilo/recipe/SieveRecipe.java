package wraith.fabricaeexnihilo.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class SieveRecipe extends BaseRecipe<SieveRecipe.Context> {
    private final ItemStack result;
    private final Ingredient input;
    private final boolean waterlogged;
    private final Map<Identifier, ? extends List<Double>> rolls;

    public SieveRecipe(Identifier id, ItemStack result, Ingredient input, boolean waterlogged, Map<Identifier, ? extends List<Double>> rolls) {
        super(id);
        this.result = result;
        this.input = input;
        this.waterlogged = waterlogged;
        this.rolls = rolls;
    }

    public static List<Loot> find(Item item, boolean waterlogged, Identifier mesh, @Nullable World world) {
        if (world == null) {
            return List.of();
        }
        return world.getRecipeManager().getAllMatches(ModRecipes.SIEVE, new Context(item, waterlogged), world)
                .stream()
                .map(recipe -> new Loot(recipe.result, recipe.rolls.get(mesh)))
                .filter(loot -> loot.chances() != null)
                .toList();
    }

    @Override
    public boolean matches(SieveRecipe.Context context, World world) {
        return input.test(context.input.getDefaultStack()) && waterlogged == context.waterlogged;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SIEVE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.SIEVE;
    }

    @Override
    public ItemStack getDisplayStack() {
        return result;
    }

    public ItemStack getResult() {
        return result;
    }

    public Map<Identifier, ? extends List<Double>> getRolls() {
        return rolls;
    }

    public Ingredient getInput() {
        return input;
    }

    public boolean isWaterlogged() {
        return waterlogged;
    }

    public record Context(Item input, boolean waterlogged) implements RecipeContext {
    }

    public static class Serializer implements RecipeSerializer<SieveRecipe> {
        @Override
        public SieveRecipe read(Identifier id, JsonObject json) {
            var result = CodecUtils.fromJson(CodecUtils.ITEM_STACK, JsonHelper.getElement(json, "result"));
            var input = Ingredient.fromJson(JsonHelper.getElement(json, "input"));
            var waterlogged = JsonHelper.getBoolean(json, "waterlogged", false);
            var rolls = new HashMap<Identifier, List<Double>>();
            JsonHelper.getObject(json, "rolls").entrySet().forEach(entry -> {
                var meshJson = entry.getKey();
                var chancesJson = entry.getValue();
                rolls.put(new Identifier(meshJson),
                        StreamSupport.stream(chancesJson.getAsJsonArray().spliterator(), false)
                                .map(JsonElement::getAsDouble)
                                .toList());
            });

            return new SieveRecipe(id, result, input, waterlogged, rolls);
        }

        @Override
        public SieveRecipe read(Identifier id, PacketByteBuf buf) {
            var result = buf.readItemStack();
            var input = Ingredient.fromPacket(buf);
            var waterlogged = buf.readBoolean();
            var rolls = buf.readMap(PacketByteBuf::readIdentifier, buf1 -> buf1.readCollection(ArrayList::new, PacketByteBuf::readDouble));

            return new SieveRecipe(id, result, input, waterlogged, rolls);
        }

        @Override
        public void write(PacketByteBuf buf, SieveRecipe recipe) {
            buf.writeItemStack(recipe.result);
            recipe.input.write(buf);
            buf.writeBoolean(recipe.waterlogged);
            buf.writeMap(recipe.rolls, PacketByteBuf::writeIdentifier, (buf1, key) -> buf1.writeCollection(key, PacketByteBuf::writeDouble));
        }
    }
}
