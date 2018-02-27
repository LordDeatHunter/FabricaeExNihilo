package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.compatibility.jei.barrel.fluidblocktransform.FluidBlockTransformRecipe;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryList;
import exnihilocreatio.registries.types.FluidBlockTransformer;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.io.FileReader;
import java.util.List;

public class FluidBlockTransformerRegistry extends BaseRegistryList<FluidBlockTransformer> {

    public FluidBlockTransformerRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson())
                        .create(),
                ExNihiloRegistryManager.FLUID_BLOCK_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(Fluid fluid, ItemInfo inputBlock, ItemInfo outputBlock) {
        registry.add(new FluidBlockTransformer(fluid.getName(), inputBlock, outputBlock));
    }

    public void register(String fluid, ItemInfo inputBlock, ItemInfo outputBlock) {
        registry.add(new FluidBlockTransformer(fluid, inputBlock, outputBlock));
    }

    public boolean canBlockBeTransformedWithThisFluid(Fluid fluid, ItemStack stack) {
        ItemInfo info = ItemInfo.getItemInfoFromStack(stack);

        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && info.equals(transformer.getInput()))
                return true;
        }
        return false;
    }

    public ItemInfo getBlockForTransformation(Fluid fluid, ItemStack stack) {
        ItemInfo info = ItemInfo.getItemInfoFromStack(stack);

        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && info.equals(transformer.getInput())) {
                return transformer.getOutput();
            }
        }

        return null;
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        List<FluidBlockTransformer> gsonInput = gson.fromJson(fr, new TypeToken<List<FluidBlockTransformer>>() {
        }.getType());
        registry.addAll(gsonInput);
    }

    @Override
    public List<FluidBlockTransformRecipe> getRecipeList() {
        List<FluidBlockTransformRecipe> fluidBlockTransformRecipes = Lists.newLinkedList();
        getRegistry().forEach(transformer -> {
            // Make sure everything's registered
            if (FluidRegistry.isFluidRegistered(transformer.getFluidName())
                    && transformer.getInput().getItem() != Items.AIR
                    && transformer.getOutput().getItem() != Items.AIR) {
                FluidBlockTransformRecipe recipe = new FluidBlockTransformRecipe(transformer);
                if (recipe.isValid()) {
                    fluidBlockTransformRecipes.add(recipe);
                }
            }
        });
        return fluidBlockTransformRecipes;
    }
}
