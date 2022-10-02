package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel;

import dev.latvian.mods.kubejs.recipe.*;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.JsonHelper;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.Color;

public class CompostRecipeJS extends RecipeJS {
    private ItemStack result;
    private Ingredient input;
    private double increment;
    private Color color;

    @Override
    public void create(RecipeArguments listJS) {
        result = parseItemOutput(listJS.get(0));
        input = parseItemInput(listJS.get(1));
        increment = 1 / (double) listJS.get(2);
        var colorJS = listJS.get(3);
        color = colorJS instanceof Integer i ? new Color(i) : new Color(colorJS.toString());
    }

    @Override
    public boolean hasInput(IngredientMatch match) {
        return match.contains(input);
    }

    @Override
    public boolean replaceInput(IngredientMatch match, Ingredient with, ItemInputTransformer transformer) {
        if (hasInput(match)) {
            input = transformer.transform(this, match, input, with);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasOutput(IngredientMatch match) {
        return match.contains(result);
    }

    @Override
    public boolean replaceOutput(IngredientMatch match, ItemStack with, ItemOutputTransformer transformer) {
        if (hasOutput(match)) {
            result = transformer.transform(this, match, result, with);
            return true;
        }
        return false;
    }

    @Override
    public void deserialize() {
        input = Ingredient.fromJson(json.get("input"));
        increment = JsonHelper.getDouble(json, "increment");
        color = Color.fromJson(json.get("color"));
        result = CodecUtils.fromJson(CodecUtils.ITEM_STACK, json.get("result"));
    }

    @Override
    public void serialize() {
        if (serializeInputs) {
            json.add("input", input.toJson());
            json.addProperty("increment", increment);
            json.addProperty("color", color.toInt());
        }
        if (serializeOutputs)
            json.add("result", CodecUtils.toJson(CodecUtils.ITEM_STACK, result));
    }
}

