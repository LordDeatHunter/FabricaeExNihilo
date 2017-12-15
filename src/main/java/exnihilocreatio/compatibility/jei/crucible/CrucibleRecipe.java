package exnihilocreatio.compatibility.jei.crucible;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nonnull;
import java.util.List;

public class CrucibleRecipe implements IRecipeWrapper {
    private List<ItemStack> inputs;
    private ItemStack output;

    public CrucibleRecipe(Fluid fluid, List<ItemStack> inputs){
        this.output = FluidUtil.getFilledBucket(new FluidStack(fluid, 1000));
        this.inputs = inputs;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, output);
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public ItemStack getFluid() {
        return output;
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

    @Override
    @Nonnull
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Lists.newArrayList();
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }

}
