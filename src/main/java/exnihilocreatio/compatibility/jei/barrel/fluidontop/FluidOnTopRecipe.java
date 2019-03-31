package exnihilocreatio.compatibility.jei.barrel.fluidontop;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import exnihilocreatio.registries.types.FluidFluidBlock;
import exnihilocreatio.util.Util;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class FluidOnTopRecipe implements IRecipeWrapper {
    private final FluidStack inputFluidInBarrel;
    private final FluidStack inputFluidOnTop;

    private final ItemStack inputBucketInBarrel;
    private final ItemStack inputBucketOnTop;

    private final ItemStack outputStack;

    public FluidOnTopRecipe(FluidFluidBlock recipe) {
        inputFluidInBarrel = new FluidStack(FluidRegistry.getFluid(recipe.getFluidInBarrel()), 1000);
        inputFluidOnTop = new FluidStack(FluidRegistry.getFluid(recipe.getFluidOnTop()), 1000);

        inputBucketInBarrel = Util.getBucketStack(inputFluidInBarrel.getFluid());
        inputBucketOnTop = Util.getBucketStack(inputFluidOnTop.getFluid());

        outputStack = recipe.getResult().getItemStack();
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, getInputs());
        ingredients.setInputs(VanillaTypes.FLUID, getFluidInputs());

        ingredients.setOutput(VanillaTypes.ITEM, outputStack);
    }

    public List<ItemStack> getInputs() {
        return ImmutableList.of(inputBucketInBarrel, inputBucketOnTop);
    }

    public List<ItemStack> getOutputs() {
        return ImmutableList.of(outputStack);
    }

    public List<FluidStack> getFluidInputs() {
        return ImmutableList.of(inputFluidInBarrel, inputFluidOnTop);
    }

    public boolean isValid() {
        return !inputBucketInBarrel.isEmpty() && !inputBucketOnTop.isEmpty() && !outputStack.isEmpty();
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
