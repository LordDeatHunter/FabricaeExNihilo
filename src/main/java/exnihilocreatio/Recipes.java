package exnihilocreatio;

import exnihilocreatio.config.ModConfig;
import exnihilocreatio.items.ItemResource;
import exnihilocreatio.items.tools.EnumCrook;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class Recipes {

    public static void init() {
        // if (ModConfig.enableBarrels) TODO: Find way to disable recipes from json

        if (ModConfig.mechanics.enableCrucible) {
            // TODO: see above
            FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(ModBlocks.crucibleStone, 1, 0), new ItemStack(ModBlocks.crucibleStone, 1, 1), 0.7f);
        }
        if(!ModConfig.crooking.disableCrookCrafting) {
            final Item clay = Item.getByNameOrId("exnihilocreatio:"+EnumCrook.CLAY.getRegistryName());
            final Item clay_uncooked = Item.getByNameOrId("exnihilocreatio:"+EnumCrook.CLAY_UNCOOKED.getRegistryName());
            FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(clay_uncooked, 1), new ItemStack(clay, 1), 0.7f);
        }
        FurnaceRecipes.instance().addSmeltingRecipe(ItemResource.getResourceStack("silkworm"), new ItemStack(ModItems.cookedSilkworm), 0.7f);
    }
}
