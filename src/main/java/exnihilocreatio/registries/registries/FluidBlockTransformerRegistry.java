package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.compatibility.jei.barrel.fluidblocktransform.FluidBlockTransformRecipe;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryList;
import exnihilocreatio.registries.types.FluidBlockTransformer;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.EntityInfo;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.StackInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;
import java.io.FileReader;
import java.util.List;

public class FluidBlockTransformerRegistry extends BaseRegistryList<FluidBlockTransformer> {

    public FluidBlockTransformerRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson())
                        .registerTypeAdapter(StackInfo.class, new CustomItemInfoJson())
                        .registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson())
                        .create(),
                ExNihiloRegistryManager.FLUID_BLOCK_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(Fluid fluid, StackInfo inputBlock, ItemInfo outputBlock) {
        if (outputBlock.hasBlock())
            registry.add(new FluidBlockTransformer(fluid.getName(), inputBlock, new BlockInfo(outputBlock.getItemStack())));
    }

    public void register(Fluid fluid, StackInfo inputBlock, ItemInfo outputBlock, String entityName) {
        if (outputBlock.hasBlock())
            registry.add(new FluidBlockTransformer(fluid.getName(), inputBlock, new BlockInfo(outputBlock.getItemStack()), entityName));
    }

    public void register(Fluid fluid, StackInfo inputBlock, BlockInfo outputBlock) {
        registry.add(new FluidBlockTransformer(fluid.getName(), inputBlock, outputBlock));
    }

    public void register(String fluid, StackInfo inputBlock, BlockInfo outputBlock) {
        registry.add(new FluidBlockTransformer(fluid, inputBlock, outputBlock));
    }

    public boolean canBlockBeTransformedWithThisFluid(Fluid fluid, ItemStack stack) {
        ItemInfo info = new ItemInfo(stack);

        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && info.equals(transformer.getInput()))
                return true;
        }
        return false;
    }

    @Nonnull
    public BlockInfo getBlockForTransformation(Fluid fluid, ItemStack stack) {
        BlockInfo info = new BlockInfo(stack);

        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && info.equals(transformer.getInput())) {
                return transformer.getOutput();
            }
        }

        return BlockInfo.EMPTY;
    }

    @Nonnull
    public int getSpawnCountForTransformation(Fluid fluid, ItemStack stack) {
        BlockInfo info = new BlockInfo(stack);

        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && info.equals(transformer.getInput())) {
                return transformer.getSpawnCount();
            }
        }

        return 0;
    }

    public EntityInfo getSpawnForTransformation(Fluid fluid, ItemStack stack) {
        BlockInfo info = new BlockInfo(stack);

        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && info.equals(transformer.getInput())) {
                return transformer.getToSpawn();
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
                    && transformer.getInput().isValid()
                    && transformer.getOutput().isValid()) {
                FluidBlockTransformRecipe recipe = new FluidBlockTransformRecipe(transformer);
                if (recipe.isValid()) {
                    fluidBlockTransformRecipes.add(recipe);
                }
            }
        });
        return fluidBlockTransformRecipes;
    }
}
