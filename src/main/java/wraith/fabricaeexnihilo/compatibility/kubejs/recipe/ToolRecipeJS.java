package wraith.fabricaeexnihilo.compatibility.kubejs.recipe;

import dev.latvian.mods.kubejs.recipe.*;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import wraith.fabricaeexnihilo.compatibility.kubejs.FENKubePlugin;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.Arrays;
import java.util.stream.StreamSupport;

@SuppressWarnings("deprecation")
public class ToolRecipeJS extends RecipeJS {

    private RegistryEntryList<Block> block;
    private Loot result;

    @Override
    public void create(RecipeArguments args) {
        result = new Loot(parseItemOutput(args.get(0)), args.list(1).list()
                .stream()
                .mapToDouble(obj -> (double) obj)
                .toArray());

        block = FENKubePlugin.getEntryList(args, 2, Registries.BLOCK);
    }

    @Override
    public boolean hasInput(IngredientMatch ingredientMatch) {
        return StreamSupport.stream(ingredientMatch.getAllItems().spliterator(), false)
                .map(ItemStack::getItem)
                .filter(BlockItem.class::isInstance)
                .map(BlockItem.class::cast)
                .map(BlockItem::getBlock)
                .anyMatch(check -> block.contains(check.getRegistryEntry()));
    }

    @Override
    public boolean replaceInput(IngredientMatch match, Ingredient with, ItemInputTransformer transformer) {
        if (hasInput(match)) {
            var oldIngredient = Ingredient.ofItems(block.stream()
                    .map(RegistryEntry::value)
                    .map(Block::asItem)
                    .toArray(Item[]::new));

            block = RegistryEntryList.of(Block::getRegistryEntry, Arrays.stream(transformer.transform(this, match, oldIngredient, with).getMatchingStacks())
                    .map(ItemStack::getItem)
                    .filter(BlockItem.class::isInstance)
                    .map(BlockItem.class::cast)
                    .map(BlockItem::getBlock)
                    .toList());
            return true;
        }

        return false;
    }

    @Override
    public boolean hasOutput(IngredientMatch ingredientMatch) {
        return ingredientMatch.contains(result.stack());
    }

    @Override
    public boolean replaceOutput(IngredientMatch match, ItemStack with, ItemOutputTransformer transformer) {
        if (match.contains(result.stack())) {
            result = new Loot(transformer.transform(this, match, result.stack(), with), result.chances());
            return true;
        }
        return false;
    }

    @Override
    public void deserialize() {
        result = CodecUtils.fromJson(Loot.CODEC, json.get("result"));
        block = RegistryEntryLists.fromJson(Registries.BLOCK.getKey(), json.get("block"));
    }

    @Override
    public void serialize() {
        if (serializeOutputs)
            json.add("result", CodecUtils.toJson(Loot.CODEC, result));
        if (serializeInputs)
            json.add("block", RegistryEntryLists.toJson(Registries.BLOCK.getKey(), block));
    }
}
