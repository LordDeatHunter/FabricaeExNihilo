package wraith.fabricaeexnihilo.api.recipes.barrel;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.util.Color;

import java.util.function.Predicate;

public record CompostRecipe(ItemIngredient ingredient,
                            ItemStack result,
                            double amount,
                            Color color) implements Predicate<ItemStack> {

    @Override
    public boolean test(ItemStack stack) {
        return ingredient.test(stack);
    }

    public void test(ItemConvertible stack) {
        ingredient.test(stack);
    }

}
