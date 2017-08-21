package exnihilocreatio;

import exnihilocreatio.config.ModConfig;
import exnihilocreatio.items.ItemResource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class Recipes {

    public static void init() {
        // if (ModConfig.enableBarrels) TODO: Find way to disable recipes from json

        if (ModConfig.mechanics.enableCrucible) {
            // TODO: see above
            FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(ModBlocks.crucible, 1, 0), new ItemStack(ModBlocks.crucible, 1, 1), 0.7f);
        }

        FurnaceRecipes.instance().addSmeltingRecipe(ItemResource.getResourceStack("silkworm"), new ItemStack(ModItems.cookedSilkworm), 0.7f);
    }
}
