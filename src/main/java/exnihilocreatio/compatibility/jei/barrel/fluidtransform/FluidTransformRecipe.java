package exnihilocreatio.compatibility.jei.barrel.fluidtransform;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import exnihilocreatio.registries.types.FluidTransformer;
import exnihilocreatio.util.BlockInfo;
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

public class FluidTransformRecipe implements IRecipeWrapper {
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;

    private final ItemStack outputBucket;

    private final List<ItemStack> inputStacks;

    public FluidTransformRecipe(FluidTransformer recipe) {
        inputFluid = new FluidStack(FluidRegistry.getFluid(recipe.getInputFluid()), 1000);
        outputFluid = new FluidStack(FluidRegistry.getFluid(recipe.getOutputFluid()), 1000);

        ItemStack inputBucket = Util.getBucketStack(inputFluid.getFluid());
        outputBucket = Util.getBucketStack(outputFluid.getFluid());

        inputStacks = Lists.newArrayList(inputBucket);

        for (BlockInfo block : recipe.getTransformingBlocks()) {
            inputStacks.add(block.getItemStack());
        }
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, inputStacks);
        ingredients.setInput(VanillaTypes.FLUID, inputFluid);

        ingredients.setOutput(VanillaTypes.ITEM, outputBucket);
        ingredients.setOutput(VanillaTypes.FLUID, outputFluid);
    }

    public List<ItemStack> getInputs() {
        return inputStacks;
    }

    public List<ItemStack> getOutputs() {
        return ImmutableList.of(outputBucket);
    }

    public List<FluidStack> getFluidInputs() {
        return ImmutableList.of(inputFluid);
    }

    public List<FluidStack> getFluidOutputs() {
        return ImmutableList.of(new FluidStack(outputFluid, 1000));
    }

    public boolean isValid() {
        return !inputStacks.isEmpty() && !outputBucket.isEmpty();
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
