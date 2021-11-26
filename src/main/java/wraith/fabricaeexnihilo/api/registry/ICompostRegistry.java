package wraith.fabricaeexnihilo.api.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.recipes.barrel.CompostRecipe;
import wraith.fabricaeexnihilo.compatibility.rei.barrel.REICompostRecipe;
import wraith.fabricaeexnihilo.util.Color;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.Collection;

public interface ICompostRegistry extends IRegistry<CompostRecipe> {

    default boolean register(ItemIngredient ingredient, ItemStack result, double amount, Color color) {
        return register(new CompostRecipe(ingredient, result, amount, color));
    }

    default boolean register(ItemConvertible ingredient, ItemStack result, double amount, Color color) {
        return register(new ItemIngredient(ingredient), result, amount, color);
    }

    default boolean register(ItemConvertible ingredient, ItemConvertible result, double amount, Color color) {
        return register(new ItemIngredient(ingredient), ItemUtils.asStack(result), amount, color);
    }

    default boolean register(Tag.Identified<Item> ingredient, ItemConvertible result, double amount, Color color) {
        return register(new ItemIngredient(ingredient), ItemUtils.asStack(result), amount, color);
    }

    CompostRecipe getRecipe(ItemStack stack);

    default boolean hasRecipe(ItemStack stack) {
        return getRecipe(stack) != null;
    }

    // All recipes, chunked/broken up for pagination
    Collection<REICompostRecipe> getREIRecipes();

}