package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.crucible;

import dev.latvian.mods.kubejs.recipe.*;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.JsonHelper;
import wraith.fabricaeexnihilo.compatibility.kubejs.FENKubePlugin;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.Arrays;
import java.util.stream.StreamSupport;

public class CrucibleHeatRecipeJS extends RecipeJS {

    private RegistryEntryList<Block> block;
    private int heat;

    @Override
    public void create(RecipeArguments args) {
        block = FENKubePlugin.getEntryList(args, 0, Registries.BLOCK);
        heat = args.getInt(1, 1);
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
        return false;
    }

    @Override
    public boolean replaceOutput(IngredientMatch ingredientMatch, ItemStack itemStack, ItemOutputTransformer itemOutputTransformer) {
        return false;
    }

    @Override
    public void deserialize() {
        block = RegistryEntryLists.fromJson(RegistryKeys.BLOCK, json.get("block"));
        heat = JsonHelper.getInt(json, "heat");
    }

    @Override
    public void serialize() {
        if (serializeInputs)
            json.add("block", RegistryEntryLists.toJson(RegistryKeys.BLOCK, block));
        if (serializeOutputs)
            json.addProperty("heat", heat);
    }
}


