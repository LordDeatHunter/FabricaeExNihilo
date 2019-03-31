package exnihilocreatio.compatibility.jei.hammer;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class HammerRecipe implements IRecipeWrapper {
    private final List<ItemStack> inputs;
    private final List<ItemStack> outputs;

    public HammerRecipe(List<ItemStack> inputs, List<ItemStack> outputs){
        this.inputs = inputs;
        this.outputs = outputs;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, inputs);
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public boolean isValid() {
        return !inputs.isEmpty() && !outputs.isEmpty();
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
