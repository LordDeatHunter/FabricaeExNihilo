package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel;

import dev.latvian.mods.kubejs.recipe.*;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import wraith.fabricaeexnihilo.compatibility.kubejs.FENKubePlugin;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.Arrays;
import java.util.stream.StreamSupport;

public class LeakingRecipeJS extends RecipeJS {
    private Block result;
    private RegistryEntryList<Fluid> fluid;
    private RegistryEntryList<Block> block;
    private long amount;

    @Override
    public void create(RecipeArguments args) {
        result = Registry.BLOCK.get(new Identifier(args.get(0).toString()));
        block = FENKubePlugin.getEntryList(args, 1, Registry.BLOCK);
        fluid = FENKubePlugin.getEntryList(args, 2, Registry.FLUID);
        amount = (long) (double) args.get(3);
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
        block = RegistryEntryLists.fromJson(Registry.BLOCK_KEY, json.get("block"));
        fluid = RegistryEntryLists.fromJson(Registry.FLUID_KEY, json.get("fluid"));
        amount = json.get("amount").getAsLong();
        result = Registry.BLOCK.get(new Identifier(json.get("result").getAsString()));
    }

    @Override
    public void serialize() {
        if (serializeInputs) {
            json.add("fluid", RegistryEntryLists.toJson(Registry.FLUID_KEY, fluid));
            json.add("block", RegistryEntryLists.toJson(Registry.BLOCK_KEY, block));
            json.addProperty("amount", amount);
        }
        if (serializeOutputs)
            json.addProperty("result", Registry.BLOCK.getId(result).toString());
    }
}

