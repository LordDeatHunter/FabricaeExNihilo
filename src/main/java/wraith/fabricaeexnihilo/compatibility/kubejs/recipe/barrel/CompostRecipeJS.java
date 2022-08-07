package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import net.minecraft.util.JsonHelper;
import wraith.fabricaeexnihilo.recipe.util.ItemIngredient;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.Color;

public class CompostRecipeJS extends RecipeJS {

    private ItemIngredient input;
    private double increment;
    private Color color;

    @Override
    public void create(RecipeArguments listJS) {
        outputItems.add(parseResultItem(listJS.get(0)));
        input = CodecUtils.fromJson(ItemIngredient.CODEC, new JsonPrimitive(listJS.get(1).toString()));
        increment = 1 / (double) listJS.get(2);
        var colorJS = listJS.get(3);
        color = colorJS instanceof Integer i ? new Color(i) : new Color(colorJS.toString());
    }

    @Override
    public void deserialize() {
        outputItems.add(ItemStackJS.of(CodecUtils.fromJson(CodecUtils.ITEM_STACK, json.get("result"))));
        input = CodecUtils.fromJson(ItemIngredient.CODEC, json.get("input"));
        increment = JsonHelper.getDouble(json, "increment");
        color = Color.fromJson(json.get("color"));
    }

    @Override
    public void serialize() {
        json.add("result", CodecUtils.toJson(CodecUtils.ITEM_STACK, outputItems.get(0).getItemStack()));
        json.add("input", CodecUtils.toJson(ItemIngredient.CODEC, input));
        json.addProperty("increment", increment);
        json.addProperty("color", color.toInt());
    }
}
