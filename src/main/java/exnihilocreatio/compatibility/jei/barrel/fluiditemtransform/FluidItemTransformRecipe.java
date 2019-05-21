package exnihilocreatio.compatibility.jei.barrel.fluiditemtransform;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import exnihilocreatio.registries.types.FluidBlockTransformer;
import exnihilocreatio.registries.types.FluidItemFluid;
import exnihilocreatio.util.Util;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FluidItemTransformRecipe implements IRecipeWrapper {

    @Nonnull
    private final FluidStack inputFluid;
    @Nonnull
    private final ItemStack inputBucket;
    @Nonnull
    private final List<ItemStack> inputStacks;

    @Nonnull
    private final ItemStack outputStack;

    public FluidItemTransformRecipe(FluidBlockTransformer recipe) {
        inputFluid = new FluidStack(FluidRegistry.getFluid(recipe.getFluidName()), 1000);

        inputBucket = Util.getBucketStack(inputFluid.getFluid());

        inputStacks = Arrays.asList(recipe.getInput().getMatchingStacks());
        outputStack = recipe.getOutput().getItemStack();
    }

    public FluidItemTransformRecipe(FluidItemFluid recipe) {
        inputFluid = new FluidStack(FluidRegistry.getFluid(recipe.getInputFluid()), 1000);
        inputBucket = Util.getBucketStack(inputFluid.getFluid());
        inputStacks = Collections.singletonList(recipe.getReactant().getItemStack());

        outputStack = Util.getBucketStack(FluidRegistry.getFluid(recipe.getOutput()));
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, getInputs());
        ingredients.setInputs(VanillaTypes.FLUID, getFluidInputs());

        ingredients.setOutput(VanillaTypes.ITEM, outputStack);
    }

    public List<List<ItemStack>> getInputs() {
        return ImmutableList.of(Collections.singletonList(inputBucket), inputStacks);
    }

    public ItemStack getOutput() {
        return outputStack;
    }

    public List<FluidStack> getFluidInputs() {
        return ImmutableList.of(inputFluid);
    }

    public boolean isValid() {
        return !inputBucket.isEmpty() && !inputStacks.isEmpty() && !outputStack.isEmpty();
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
