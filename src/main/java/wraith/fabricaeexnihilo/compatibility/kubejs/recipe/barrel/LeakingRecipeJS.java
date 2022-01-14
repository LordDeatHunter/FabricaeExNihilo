package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.util.CodecUtils;

public class LeakingRecipeJS extends RecipeJS {
    private BlockIngredient block;
    private FluidIngredient fluid;
    private long amount;
    private Block result;
    
    @Override
    public void create(ListJS listJS) {
        result = Registry.BLOCK.get(new Identifier(listJS.get(0).toString()));
        block = CodecUtils.fromJson(BlockIngredient.CODEC, new JsonPrimitive(listJS.get(1).toString()));
        fluid = CodecUtils.fromJson(FluidIngredient.CODEC, new JsonPrimitive(listJS.get(2).toString()));
        amount = (long) listJS.get(3);
    }
    
    @Override
    public void deserialize() {
        block = CodecUtils.fromJson(BlockIngredient.CODEC, json.get("block"));
        fluid = CodecUtils.fromJson(FluidIngredient.CODEC, json.get("fluid"));
        amount = json.get("amount").getAsLong();
        result = Registry.BLOCK.get(new Identifier(json.get("result").getAsString()));
    }
    
    @Override
    public void serialize() {
        json.add("block", CodecUtils.toJson(BlockIngredient.CODEC, block));
        json.add("fluid", CodecUtils.toJson(FluidIngredient.CODEC, fluid));
        json.addProperty("amount", amount);
        json.addProperty("result", Registry.BLOCK.getId(result).toString());
    }
}
