package wraith.fabricaeexnihilo.compatibility.kubejs.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.*;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class SieveRecipeJS extends RecipeJS {
    private final Map<Identifier, List<Double>> rolls = new HashMap<>();
    private ItemStack result;
    private Ingredient input;

    @Override
    public void create(RecipeArguments args) {
        result = parseItemOutput(args.get(0));
        input = parseItemInput(args.get(1));
        MapJS.orEmpty(args.get(3)).forEach((id, rolls) -> this.rolls.put(Identifier.tryParse((String) id), (ListJS.orEmpty(rolls)).stream().map(obj -> (Double) obj).toList()));
    }

    public SieveRecipeJS waterlogged(boolean waterlogged) {
        this.json.addProperty("waterlogged", waterlogged);
        this.save();
        return this;
    }

    public SieveRecipeJS waterlogged() {
        return waterlogged(true);
    }

    @Override
    public boolean hasInput(IngredientMatch match) {
        return match.contains(input);
    }

    @Override
    public boolean replaceInput(IngredientMatch match, Ingredient with, ItemInputTransformer transformer) {
        if (match.contains(input)) {
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
        if (match.contains(result)) {
            result = transformer.transform(this, match, result, with);
            return true;
        }

        return false;
    }

    @Override
    public void deserialize() {
        result = CodecUtils.fromJson(CodecUtils.ITEM_STACK, JsonHelper.getElement(json, "result"));
        input = Ingredient.fromJson(JsonHelper.getElement(json, "input"));
        JsonHelper.getObject(json, "rolls").entrySet().forEach(entry -> {
            var meshJson = entry.getKey();
            var chancesJson = entry.getValue();
            rolls.put(new Identifier(meshJson),
                    StreamSupport.stream(chancesJson.getAsJsonArray().spliterator(), false)
                            .map(JsonElement::getAsDouble)
                            .toList());
        });
    }

    @Override
    public void serialize() {
        if (serializeOutputs)
            json.add("result", CodecUtils.toJson(CodecUtils.ITEM_STACK, result));
        if (serializeInputs) {
            json.add("input", input.toJson());
            var rollsJson = new JsonObject();
            rolls.forEach((mesh, chances) -> {
                var chancesJson = new JsonArray();
                chances.forEach(chancesJson::add);
                rollsJson.add(mesh.toString(), chancesJson);
            });
            json.add("rolls", rollsJson);
        }
    }
}
