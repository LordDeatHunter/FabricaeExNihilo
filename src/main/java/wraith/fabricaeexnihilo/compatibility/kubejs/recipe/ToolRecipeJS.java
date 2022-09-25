/*package wraith.fabricaeexnihilo.compatibility.kubejs.recipe;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.recipe.*;
import dev.latvian.mods.kubejs.util.ListJS;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ToolRecipeJS extends RecipeJS {

    private BlockIngredient block = new BlockIngredient(Blocks.AIR);
    private Loot result = Loot.EMPTY;

    @Override
    public void create(RecipeArguments args) {
        result = new Loot(parseItemOutput(args.get(0)), args.list(1).list()
            .stream()
            .mapToDouble(obj -> (double) obj)
            .toArray());
        block = CodecUtils.fromJson(BlockIngredient.CODEC, new JsonPrimitive(args.get(2).toString()));
    }

    @Override
    public boolean hasInput(IngredientMatch ingredientMatch) {
        return StreamSupport.stream(ingredientMatch.getAllItems().spliterator(), false)
                .map(ItemStack::getItem)
                .filter(BlockItem.class::isInstance)
                .map(BlockItem.class::cast)
                .map(BlockItem::getBlock)
                .anyMatch(block);
    }

    @Override
    public boolean replaceInput(IngredientMatch match, Ingredient with, ItemInputTransformer transformer) {
        if (hasInput(match)) {
            var oldIngredient = Ingredient.ofItems(block.streamEntries()
                    .map(Block::asItem)
                    .filter(item -> item != Items.AIR).toArray(Item[]::new));

            transformer.transform(this, match, oldIngredient, with).getMatchingStacks();
        }

        return false;
    }

    @Override
    public boolean hasOutput(IngredientMatch ingredientMatch) {
        return ingredientMatch.contains(result.stack());
    }

    @Override
    public boolean replaceOutput(IngredientMatch ingredientMatch, ItemStack itemStack, ItemOutputTransformer itemOutputTransformer) {
        return false;
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
}*/
