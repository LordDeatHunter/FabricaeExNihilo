package exnihilocreatio.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.Objects;

public class IngredientUtil {
    public static Ingredient parseFromString(String s) {
        if (s.startsWith("ore:")) {
            s = s.substring(4);
            return new OreIngredientStoring(s);
        } else {
            String[] split = s.split(":");
            if (split.length >= 2) {
                Item item = Item.getByNameOrId(split[0]+ ":" + split[1]);

                if (item == null) {
                    LogUtil.error("Error parsing Ingredient String: Invalid Item: " + s);

                    return Ingredient.EMPTY;
                }

                int meta;
                if (split.length >= 3) {
                    try {
                        meta = Integer.parseInt(split[2]);
                        ItemStack stack = new ItemStack(item, 1, meta);
                        return CraftingHelper.getIngredient(stack);

                    } catch (NumberFormatException ignored){
                        LogUtil.error("Number error in json: " + s);
                    }
                } else {
                    return CraftingHelper.getIngredient(item);
                }
            }
        }

        return Ingredient.EMPTY;
    }

    public static boolean ingredientEquals(Ingredient ingr1, Ingredient ingr2) {
        if (ingr1 instanceof OreIngredientStoring && ingr2 instanceof OreIngredientStoring) {
            return Objects.equals(((OreIngredientStoring) ingr1).getOreName(), ((OreIngredientStoring) ingr2).getOreName());
        }

        return Util.arrayEqualsPredicate(ingr1.getMatchingStacks(), ingr2.getMatchingStacks(), ItemStack::areItemStacksEqual);
    }


}
