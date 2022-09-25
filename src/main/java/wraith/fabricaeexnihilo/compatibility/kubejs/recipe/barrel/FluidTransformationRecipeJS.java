/*
package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.MapJS;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.util.CodecUtils;

public class FluidTransformationRecipeJS extends RecipeJS {

    private FluidIngredient contained;
    private BlockIngredient catalyst;
    private BarrelMode result;

    @Override
    public void create(RecipeArguments listJS) {
        result = CodecUtils.fromJson(BarrelMode.CODEC, MapJS.json(listJS.get(0)));
        contained = CodecUtils.fromJson(FluidIngredient.CODEC, new JsonPrimitive(listJS.get(1).toString()));
        catalyst = CodecUtils.fromJson(BlockIngredient.CODEC, new JsonPrimitive(listJS.get(2).toString()));
    }

    @Override
    public void deserialize() {
        result = CodecUtils.fromJson(BarrelMode.CODEC, json.get("result"));
        contained = CodecUtils.fromJson(FluidIngredient.CODEC, json.get("contained"));
        catalyst = CodecUtils.fromJson(BlockIngredient.CODEC, json.get("catalyst"));
    }

    @Override
    public void serialize() {
        json.add("result", CodecUtils.toJson(BarrelMode.CODEC, result));
        json.add("contained", CodecUtils.toJson(FluidIngredient.CODEC, contained));
        json.add("catalyst", CodecUtils.toJson(BlockIngredient.CODEC, catalyst));
    }
}
*/
