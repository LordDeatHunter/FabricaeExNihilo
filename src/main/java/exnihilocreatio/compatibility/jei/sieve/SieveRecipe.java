package exnihilocreatio.compatibility.jei.sieve;

import com.google.common.collect.Lists;
import exnihilocreatio.ModItems;
import exnihilocreatio.blocks.BlockSieve.MeshType;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.Siftable;
import exnihilocreatio.util.BlockInfo;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SieveRecipe implements IRecipeWrapper {
    private List<ItemStack> inputs = new ArrayList<>();
    private List<ItemStack> outputs = new ArrayList<>();

    public SieveRecipe(IBlockState block, MeshType mesh) {
        List<Siftable> rewards = ExNihiloRegistryManager.SIEVE_REGISTRY.getDrops(new BlockInfo(block));
        // Filter reward list into item stack list, keeping only those of the correct mesh level

        if (rewards == null) {
            return;
        }

        List<ItemStack> allOutputs = Lists.newArrayList(rewards.stream().filter(reward -> reward.getMeshLevel() == mesh.getID()).map(reward -> reward.getDrop().getItemStack()).collect(Collectors.toList()));
        // Make sure no null rewards, Item or ItemStack
        //allOutputs.removeIf(stack -> stack == ItemStack.EMPTY);

        inputs = Lists.newArrayList(new ItemStack(ModItems.mesh, 1, mesh.getID()), new ItemStack(block.getBlock(), 1, block.getBlock().getMetaFromState(block)));
        outputs = Lists.newArrayList();

        for (ItemStack stack : allOutputs) {
            boolean alreadyExists = false;

            for (ItemStack outputStack : outputs) {
                if (stack.getItem().equals(outputStack.getItem()) && stack.getMetadata() == outputStack.getMetadata()) {
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
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, outputs);
    }

    public List getInputs() {
        return inputs;
    }

    public List getOutputs() {
        return outputs;
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
