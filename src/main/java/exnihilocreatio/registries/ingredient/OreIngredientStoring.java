package exnihilocreatio.registries.ingredient;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nonnull;

/**
 * Exactly the same as OreIngredient, but it still has the name in it so we can store it to JSON, YAML or whatever.
 */
public class OreIngredientStoring extends OreIngredient {
    private String oreName;

    public OreIngredientStoring(String ore) {
        super(ore);
        oreName = ore;
    }

    public String getOreName() {
        return oreName;
    }

    @Override
    public boolean apply(ItemStack input) {
        if (input == null)
            return false;
        if (!OreDictionary.doesOreNameExist(oreName))
            return false;

        for (ItemStack stack : OreDictionary.getOres(oreName, false)) {
            if (OreDictionary.itemMatches(stack, input, false))
                return true;
        }

        return false;
    }

    @Nonnull
    @Override
    public ItemStack[] getMatchingStacks() {
        NonNullList<ItemStack> items = OreDictionary.getOres(oreName);
        NonNullList<ItemStack> list = NonNullList.create();

        if (items != null && !items.isEmpty()) {
            for (ItemStack itemstack : items)
            {
                if (itemstack.getMetadata() == OreDictionary.WILDCARD_VALUE)
                    itemstack.getItem().getSubItems(CreativeTabs.SEARCH, list);
                else
                    list.add(itemstack);
            }
        }

        return list.toArray(new ItemStack[0]);
    }
}
