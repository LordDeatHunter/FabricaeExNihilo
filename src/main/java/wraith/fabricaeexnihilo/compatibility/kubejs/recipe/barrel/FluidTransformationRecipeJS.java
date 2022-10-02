package wraith.fabricaeexnihilo.compatibility.kubejs.recipe.barrel;

import dev.latvian.mods.kubejs.recipe.*;
import dev.latvian.mods.kubejs.util.MapJS;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import wraith.fabricaeexnihilo.compatibility.kubejs.FENKubePlugin;
import wraith.fabricaeexnihilo.modules.barrels.modes.BarrelMode;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.Arrays;
import java.util.stream.StreamSupport;

public class FluidTransformationRecipeJS extends RecipeJS {
    private RegistryEntryList<Fluid> fluid;
    private RegistryEntryList<Block> catalyst;
    private BarrelMode result;

    @Override
    public void create(RecipeArguments args) {
        result = CodecUtils.fromJson(BarrelMode.CODEC, MapJS.json(args.get(0)));
        fluid = FENKubePlugin.getEntryList(args, 1, Registry.FLUID);
        catalyst = FENKubePlugin.getEntryList(args, 2, Registry.BLOCK);
    }

    @Override
    public boolean hasInput(IngredientMatch ingredientMatch) {
        return StreamSupport.stream(ingredientMatch.getAllItems().spliterator(), false)
                .map(ItemStack::getItem)
                .filter(BlockItem.class::isInstance)
                .map(BlockItem.class::cast)
                .map(BlockItem::getBlock)
                .anyMatch(check -> catalyst.contains(check.getRegistryEntry()));
    }

    @Override
    public boolean replaceInput(IngredientMatch match, Ingredient with, ItemInputTransformer transformer) {
        if (hasInput(match)) {
            var oldIngredient = Ingredient.ofItems(catalyst.stream()
                    .map(RegistryEntry::value)
                    .map(Block::asItem)
                    .toArray(Item[]::new));

            catalyst = RegistryEntryList.of(Block::getRegistryEntry, Arrays.stream(transformer.transform(this, match, oldIngredient, with).getMatchingStacks())
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
        result = CodecUtils.fromJson(BarrelMode.CODEC, json.get("result"));
        fluid = RegistryEntryLists.fromJson(Registry.FLUID_KEY, json.get("fluid"));
        catalyst = RegistryEntryLists.fromJson(Registry.BLOCK_KEY, json.get("catalyst"));
    }

    @Override
    public void serialize() {
        if (serializeInputs) {
            json.add("fluid", RegistryEntryLists.toJson(Registry.FLUID_KEY, fluid));
            json.add("catalyst", RegistryEntryLists.toJson(Registry.BLOCK_KEY, catalyst));
        }
        if (serializeOutputs) {
            json.add("result", CodecUtils.toJson(BarrelMode.CODEC, result));
        }
    }
}

