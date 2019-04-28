package exnihilocreatio.compatibility.jei.sieve;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SieveRecipe implements IRecipeWrapper {
    private List<List<ItemStack>> inputs;
    private List<ItemStack> outputs;

    public SieveRecipe(List<List<ItemStack>> inputs, List<ItemStack> outputs){
        this.inputs = inputs;
        this.outputs = outputs;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }

    public ItemStack getMesh() {
        return inputs.get(0).get(0);
    }

    public List<ItemStack> getSievables() {
        return inputs.get(1);
    }

    public List getInputs() {
        return inputs;
    }

    public List getOutputs() {
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
        return new ArrayList<>();
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}
