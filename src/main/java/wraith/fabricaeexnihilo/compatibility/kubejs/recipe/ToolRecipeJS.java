package wraith.fabricaeexnihilo.compatibility.kubejs.recipe;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import net.minecraft.block.Blocks;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.util.CodecUtils;

public class ToolRecipeJS extends RecipeJS {
    private BlockIngredient block = new BlockIngredient(Blocks.AIR);
    private Loot result = Loot.EMPTY;
    
    @Override
    public void create(ListJS listJS) {
        result = new Loot(parseResultItem(listJS.get(0)).getItemStack(), ListJS.orSelf(listJS.get(1))
                .stream()
                .map(obj -> (double)obj)
                .toList());
        block = CodecUtils.fromJson(BlockIngredient.CODEC, new JsonPrimitive(listJS.get(2).toString()));
    }
    
    @Override
    public void deserialize() {
        result = CodecUtils.fromJson(Loot.CODEC, json.get("result"));
        block = CodecUtils.fromJson(BlockIngredient.CODEC, json.get("block"));
    }
    
    @Override
    public void serialize() {
        json.add("result", CodecUtils.toJson(Loot.CODEC, result));
        json.add("block", CodecUtils.toJson(BlockIngredient.CODEC, block));
    }
}
