package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.MapJS;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.util.CodecUtils;

public class FluidCombinationRecipeJS extends RecipeJS {

    private BarrelMode result;
    private FluidIngredient contained;
    private FluidIngredient other;

    @Override
    public void create(RecipeArguments listJS) {
        result = CodecUtils.fromJson(BarrelMode.CODEC, MapJS.json(listJS.get(0)));
        contained = CodecUtils.fromJson(FluidIngredient.CODEC, new JsonPrimitive(listJS.get(1).toString()));
        other = CodecUtils.fromJson(FluidIngredient.CODEC, new JsonPrimitive(listJS.get(2).toString()));
    }

    @Override
    public void deserialize() {
        result = CodecUtils.fromJson(BarrelMode.CODEC, json.get("result"));
        contained = CodecUtils.fromJson(FluidIngredient.CODEC, json.get("contained"));
        other = CodecUtils.fromJson(FluidIngredient.CODEC, json.get("other"));
    }

    @Override
    public void serialize() {
        json.add("result", CodecUtils.toJson(BarrelMode.CODEC, result));
        json.add("contained", CodecUtils.toJson(FluidIngredient.CODEC, contained));
        json.add("other", CodecUtils.toJson(FluidIngredient.CODEC, other));
    }
}
