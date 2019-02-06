package exnihilocreatio.compatibility.jei.sieve;

import com.google.common.collect.Lists;
import exnihilocreatio.ModItems;
import exnihilocreatio.blocks.BlockSieve.MeshType;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.Siftable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SieveRecipe implements IRecipeWrapper {
    private ItemStack mesh;
    private List<ItemStack> inputs = new ArrayList<>();
    private List<ItemStack> outputs = new ArrayList<>();

    public SieveRecipe(MeshType mesh, List<ItemStack> inputs, List<ItemStack> outputs){
        this.mesh = new ItemStack(ModItems.mesh, 1, mesh.getID());
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public SieveRecipe(Ingredient ingredient, MeshType mesh) {
        List<Siftable> rewards = ExNihiloRegistryManager.SIEVE_REGISTRY.getDrops(ingredient);
        // Filter reward list into item stack list, keeping only those of the correct mesh level

        if (rewards.isEmpty())
            return;

        List<ItemStack> allOutputs = rewards.stream()
                .filter(reward -> reward.getMeshLevel() == mesh.getID())
                .map(reward -> reward.getDrop().getItemStack())
                .collect(Collectors.toList());

        // Make sure no null rewards, Item or ItemStack
        //allOutputs.removeIf(stack -> stack == ItemStack.EMPTY);

        this.mesh = new ItemStack(ModItems.mesh, 1, mesh.getID());
        inputs = Arrays.asList(ingredient.getMatchingStacks());
        outputs = Lists.newArrayList();

        for (ItemStack stack : allOutputs) {
            boolean alreadyExists = false;

            for (ItemStack outputStack : outputs) {
                if (ItemStack.areItemsEqual(stack, outputStack) && ItemStack.areItemStackTagsEqual(stack, outputStack)) {
                    outputStack.grow(stack.getCount());
                    alreadyExists = true;
                    break;
                }
            }

            if (!alreadyExists) {
                outputs.add(stack);
            }
        }
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, outputs);
    }

    public ItemStack getMesh() {
        return mesh;
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
