package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.witchwater;

import dev.latvian.mods.kubejs.recipe.*;
import dev.latvian.mods.kubejs.util.MapJS;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import wraith.fabricaeexnihilo.compatibility.kubejs.FENKubePlugin;
import wraith.fabricaeexnihilo.recipe.util.WeightedList;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.HashMap;

public class WitchWaterWorldRecipeJS extends RecipeJS {

    private WeightedList result;
    private RegistryEntryList<Fluid> target;

    @Override
    public void create(RecipeArguments listJS) {
        result = new WeightedList((MapJS.orEmpty(listJS.get(0))).entrySet()
                .stream()
                .map(entry -> new Pair<>(Registries.BLOCK.get(new Identifier((String) entry.getKey())), ((Number) entry.getValue()).intValue()))
                .toList());
        target = FENKubePlugin.getEntryList(listJS, 1, Registries.FLUID);
    }

    @Override
    public boolean hasInput(IngredientMatch ingredientMatch) {
        return false;
    }

    @Override
    public boolean replaceInput(IngredientMatch ingredientMatch, Ingredient ingredient, ItemInputTransformer itemInputTransformer) {
        return false;
    }

    @Override
    public boolean hasOutput(IngredientMatch match) {
        return result.asListOfStacks().stream().anyMatch(match::contains);
    }

    @Override
    public boolean replaceOutput(IngredientMatch match, ItemStack with, ItemOutputTransformer transformer) {
        if (hasOutput(match)) {
            var map = new HashMap<Block, Integer>();
            result.getValues().forEach((block, weight) -> {
                var stack = transformer.transform(this, match, block.asItem().getDefaultStack(), with);
                if (stack.getItem() instanceof BlockItem blockItem) {
                    var newBlock = blockItem.getBlock();
                    map.put(newBlock, map.computeIfAbsent(newBlock, __ -> 0) + weight);
                }
            });
            return result.equals(result = new WeightedList(map));
        }
        return false;
    }

    @Override
    public void deserialize() {
        target = RegistryEntryLists.fromJson(Registries.FLUID.getKey(), JsonHelper.getElement(json, "target"));
        result = CodecUtils.fromJson(WeightedList.CODEC, JsonHelper.getElement(json, "result"));
    }

    @Override
    public void serialize() {
        if (serializeOutputs)
            json.add("result", CodecUtils.toJson(WeightedList.CODEC, result));
        if (serializeInputs)
            json.add("target", RegistryEntryLists.toJson(Registries.FLUID.getKey(), target));
    }
}

