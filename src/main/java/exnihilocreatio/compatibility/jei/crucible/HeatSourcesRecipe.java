package exnihilocreatio.compatibility.jei.crucible;

import exnihilocreatio.util.BlockInfo;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.config.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HeatSourcesRecipe implements IRecipeWrapper {
    private final List<List<ItemStack>> inputs;
    private final String heatAmountString;

    private final IDrawableAnimated flame;

    public HeatSourcesRecipe(IGuiHelper guiHelper, BlockInfo blockInfo, int heatAmount) {

        ItemStack item = blockInfo.getItemStack();
        System.out.println("item = " + item);

        List<ItemStack> inputList = new ArrayList<>(Collections.singleton(item));
        this.inputs = Collections.singletonList(inputList);

        heatAmountString = String.valueOf(heatAmount);

        IDrawableStatic flameDrawable = guiHelper.createDrawable(Constants.RECIPE_GUI_VANILLA, 82, 114, 14, 14);
        this.flame = guiHelper.createAnimatedDrawable(flameDrawable, heatAmount, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, inputs);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        flame.draw(minecraft, 1, 0);
        minecraft.fontRenderer.drawString(heatAmountString, 24, 8, Color.gray.getRGB());
        // minecraft.fontRenderer.drawString(burnTimeString, 24, 18, Color.gray.getRGB());
    }


}
