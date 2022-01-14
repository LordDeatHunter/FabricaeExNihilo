package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.witchwater;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.recipe.util.WeightedList;
import wraith.fabricaeexnihilo.util.CodecUtils;

public class WitchWaterWorldRecipeJS extends RecipeJS {
    private FluidIngredient target;
    private WeightedList result;
    
    @Override
    public void create(ListJS listJS) {
        result = new WeightedList(((MapJS)listJS.get(0)).entrySet()
                .stream()
                .map(entry -> new Pair<>(Registry.BLOCK.get(new Identifier(entry.getKey())), (int) entry.getValue()))
                .toList());
        target = CodecUtils.fromJson(FluidIngredient.CODEC, new JsonPrimitive(listJS.get(1).toString()));
    }
    
    @Override
    public void deserialize() {
        target = CodecUtils.fromJson(FluidIngredient.CODEC, json.get("target"));
        result = CodecUtils.fromJson(WeightedList.CODEC, json.get("result"));
    }
    
    @Override
    public void serialize() {
        json.add("target", CodecUtils.toJson(FluidIngredient.CODEC, target));
        json.add("result", CodecUtils.toJson(WeightedList.CODEC, result));
    }
}
