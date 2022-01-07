package wraith.fabricaeexnihilo.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.ModRecipes;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.recipe.util.ItemIngredient;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SieveRecipe extends BaseRecipe<SieveRecipe.Context> {
    private final ItemStack result;
    private final ItemIngredient input;
    private final FluidIngredient fluid;
    private final Map<Identifier, ? extends List<Double>> rolls;
    
    public SieveRecipe(Identifier id, ItemStack result, ItemIngredient input, FluidIngredient fluid, Map<Identifier, ? extends List<Double>> rolls) {
        super(id);
        this.result = result;
        this.input = input;
        this.fluid = fluid;
        this.rolls = rolls;
    }
    
    public static List<Loot> find(Item item, Fluid fluid, Identifier mesh, @Nullable World world) {
        if (world == null) {
            return List.of();
        }
        return world.getRecipeManager().getAllMatches(ModRecipes.SIEVE, new Context(item, fluid), world)
                .stream()
                .map(recipe -> new Loot(recipe.result, recipe.rolls.get(mesh)))
                .filter(loot -> loot.chances() != null)
                .toList();
    }
    
    @Override
    public boolean matches(SieveRecipe.Context context, World world) {
        return input.test(context.input) && fluid.test(context.fluid);
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
    
    public ItemIngredient getInput() {
        return input;
    }
    
    public FluidIngredient getFluid() {
        return fluid;
    }
    
    public List<Double> getRollsForMesh(Identifier mesh) {
        return rolls.get(mesh);
    }
    
    public static record Context(Item input, Fluid fluid) implements RecipeContext {
    }
    
    public static class Serializer implements RecipeSerializer<SieveRecipe> {
        @Override
        public SieveRecipe read(Identifier id, JsonObject json) {
            var result = CodecUtils.fromJson(CodecUtils.ITEM_STACK, json.get("result"));
            var input = CodecUtils.fromJson(ItemIngredient.CODEC, json.get("input"));
            var fluid = json.has("fluid") ? CodecUtils.fromJson(FluidIngredient.CODEC, json.get("fluid")) : new FluidIngredient(Fluids.EMPTY);
            var rolls = JsonHelper.getObject(json, "rolls")
                    .entrySet()
                    .stream()
                    .map(entry -> new Pair<>(new Identifier(entry.getKey()),
                            StreamSupport.stream(entry.getValue()
                                            .getAsJsonArray()
                                            .spliterator(), false)
                                    .map(JsonElement::getAsDouble)
                                    .toList()))
                    .collect(Collectors.toUnmodifiableMap(Pair::getLeft, Pair::getRight));
            
            return new SieveRecipe(id, result, input, fluid, rolls);
        }
        
        @Override
        public SieveRecipe read(Identifier id, PacketByteBuf buf) {
            var result = buf.readItemStack();
            var input = CodecUtils.fromPacket(ItemIngredient.CODEC, buf);
            var fluid = CodecUtils.fromPacket(FluidIngredient.CODEC, buf);
            var rolls = buf.readMap(PacketByteBuf::readIdentifier, buf1 -> buf1.readCollection(ArrayList::new, PacketByteBuf::readDouble));
            
            return new SieveRecipe(id, result, input, fluid, rolls);
        }
        
        @Override
        public void write(PacketByteBuf buf, SieveRecipe recipe) {
            buf.writeItemStack(recipe.result);
            CodecUtils.toPacket(ItemIngredient.CODEC, recipe.input, buf);
            CodecUtils.toPacket(FluidIngredient.CODEC, recipe.fluid, buf);
            buf.writeMap(recipe.rolls, PacketByteBuf::writeIdentifier, (buf1, key) -> buf1.writeCollection(key, PacketByteBuf::writeDouble));
        }
    }
}
