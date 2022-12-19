package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel;

import dev.latvian.mods.kubejs.recipe.*;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
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
        result = Registries.BLOCK.get(new Identifier(args.get(0).toString()));
        block = FENKubePlugin.getEntryList(args, 1, Registries.BLOCK);
        fluid = FENKubePlugin.getEntryList(args, 2, Registries.FLUID);
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
        block = RegistryEntryLists.fromJson(Registries.BLOCK.getKey(), json.get("block"));
        fluid = RegistryEntryLists.fromJson(Registries.FLUID.getKey(), json.get("fluid"));
        amount = json.get("amount").getAsLong();
        result = Registries.BLOCK.get(new Identifier(json.get("result").getAsString()));
    }

    @Override
    public void serialize() {
        if (serializeInputs) {
            json.add("fluid", RegistryEntryLists.toJson(Registries.FLUID.getKey(), fluid));
            json.add("block", RegistryEntryLists.toJson(Registries.BLOCK.getKey(), block));
            json.addProperty("amount", amount);
        }
        if (serializeOutputs)
            json.addProperty("result", Registries.BLOCK.getId(result).toString());
    }
}

