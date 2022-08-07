package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.crucible;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import net.minecraft.util.JsonHelper;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.util.CodecUtils;

public class CrucibleHeatRecipeJS extends RecipeJS {

    private BlockIngredient block;
    private int heat;

    @Override
    public void create(RecipeArguments listJS) {
        block = CodecUtils.fromJson(BlockIngredient.CODEC, new JsonPrimitive(listJS.get(0).toString()));
        heat = (int) (double) listJS.get(1);
    }

    @Override
    public void deserialize() {
        block = CodecUtils.fromJson(BlockIngredient.CODEC, json.get("block"));
        heat = JsonHelper.getInt(json, "heat");
    }

    @Override
    public void serialize() {
        json.add("block", CodecUtils.toJson(BlockIngredient.CODEC, block));
        json.addProperty("heat", heat);
    }
}
