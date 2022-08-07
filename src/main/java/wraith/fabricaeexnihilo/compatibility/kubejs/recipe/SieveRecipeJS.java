package wraith.fabricaeexnihilo.compatibility.kubejs.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.recipe.util.ItemIngredient;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class SieveRecipeJS extends RecipeJS {

    private final Map<Identifier, List<Double>> rolls = new HashMap<>();
    private ItemIngredient input = ItemIngredient.EMPTY;
    private FluidIngredient fluid = new FluidIngredient(Fluids.EMPTY);

    @Override
    public void create(RecipeArguments listJS) {
        outputItems.add(parseResultItem(listJS.get(0)));
        input = CodecUtils.fromJson(ItemIngredient.CODEC, new JsonPrimitive(listJS.get(1).toString()));
        fluid = CodecUtils.fromJson(FluidIngredient.CODEC, new JsonPrimitive(listJS.get(2).toString()));
        MapJS.orEmpty(listJS.get(3)).forEach((id, rolls) -> this.rolls.put(Identifier.tryParse((String) id), (ListJS.orEmpty(rolls)).stream().map(obj -> (Double) obj).toList()));
    }

    @Override
    public void deserialize() {
        outputItems.add(ItemStackJS.of(CodecUtils.fromJson(CodecUtils.ITEM_STACK, json.get("result"))));
        input = CodecUtils.fromJson(ItemIngredient.CODEC, json.get("input"));
        fluid = json.has("fluid") ? CodecUtils.fromJson(FluidIngredient.CODEC, json.get("fluid")) : new FluidIngredient(Fluids.EMPTY);
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
        json.add("result", CodecUtils.toJson(CodecUtils.ITEM_STACK, outputItems.get(0).getItemStack()));
        json.add("input", CodecUtils.toJson(ItemIngredient.CODEC, input));
        json.add("fluid", CodecUtils.toJson(FluidIngredient.CODEC, fluid));
        var rollsJson = new JsonObject();
        rolls.forEach((mesh, chances) -> {
            var chancesJson = new JsonArray();
            chances.forEach(chancesJson::add);
            rollsJson.add(mesh.toString(), chancesJson);
        });
        json.add("rolls", rollsJson);
    }
}
