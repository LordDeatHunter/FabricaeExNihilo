package exnihilocreatio.registries.registries;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.api.registries.IFluidBlockTransformerRegistry;
import exnihilocreatio.compatibility.jei.barrel.fluidblocktransform.FluidBlockTransformRecipe;
import exnihilocreatio.json.*;
import exnihilocreatio.registries.ingredient.OreIngredientStoring;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryList;
import exnihilocreatio.registries.types.FluidBlockTransformer;
import exnihilocreatio.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FluidBlockTransformerRegistry extends BaseRegistryList<FluidBlockTransformer> implements IFluidBlockTransformerRegistry {

    public FluidBlockTransformerRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson())
                        .registerTypeAdapter(StackInfo.class, new CustomItemInfoJson())
                        .registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson())
                        .registerTypeAdapter(Ingredient.class, new CustomIngredientJson())
                        .registerTypeAdapter(OreIngredientStoring.class, new CustomIngredientJson())
                        .registerTypeAdapter(EntityInfo.class, new CustomEntityInfoJson())
                        .registerTypeAdapter(FluidBlockTransformer.class, new CustomFluidBlockTransformerJson())
                        .create(),
                ExNihiloRegistryManager.FLUID_BLOCK_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(Fluid fluid, StackInfo inputBlock, StackInfo outputBlock) {
        register(fluid, inputBlock, outputBlock, null);
    }

    public void register(Fluid fluid, StackInfo inputBlock, StackInfo outputBlock, String entityName) {
        if (fluid == null){
            LogUtil.error("Fluid is null, this may not happen!");
            for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
                LogUtil.warn(stackTraceElement);
            }

            return;
        }


        register(fluid.getName(), Ingredient.fromStacks(inputBlock.getItemStack()), outputBlock, entityName, entityName == null ? 0 : 4, entityName == null ? 0 : 4);
    }

    public void register(Fluid fluid, String oredict, StackInfo outputBlock) {
        register(fluid, oredict, outputBlock, null);
    }

    public void register(Fluid fluid, String oredict, StackInfo outputBlock, String entityName) {
        register(fluid.getName(), new OreIngredientStoring(oredict), outputBlock, entityName, entityName == null ? 0 : 4, entityName == null ? 0 : 4);
    }

    /**
     * Main register function
     */
    public void register(@Nonnull String fluid, @Nonnull Ingredient input, @Nonnull StackInfo outputBlock, @Nullable String entityName, int spawnCount, int spawnRange) {
        if (outputBlock.hasBlock()) {
            register(new FluidBlockTransformer(fluid, input, new BlockInfo(outputBlock.getBlockState()), entityName, spawnCount, spawnRange));
        } else {
            LogUtil.error("Item " + outputBlock.toString() + "  has no block version!");
        }
    }


    public boolean canBlockBeTransformedWithThisFluid(Fluid fluid, ItemStack stack) {
        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && transformer.getInput().apply(stack))
                return true;
        }

        return false;
    }

    @Nonnull
    public BlockInfo getBlockForTransformation(Fluid fluid, ItemStack stack) {
        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && transformer.getInput().apply(stack)) {
                return transformer.getOutput();
            }
        }

        return BlockInfo.EMPTY;
    }

    public int getSpawnCountForTransformation(Fluid fluid, ItemStack stack) {
        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && transformer.getInput().apply(stack)) {
                return transformer.getSpawnCount();
            }
        }

        return 0;
    }

    public int getSpawnRangeForTransformation(Fluid fluid, ItemStack stack) {
        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && transformer.getInput().apply(stack)) {
                return transformer.getSpawnRange();
            }
        }

        return 0;
    }

    public FluidBlockTransformer getTransformation(Fluid fluid, ItemStack stack) {
        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && transformer.getInput().apply(stack)) {
                return transformer;
            }
        }

        return null;
    }

    public EntityInfo getSpawnForTransformation(Fluid fluid, ItemStack stack) {
        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && transformer.getInput().apply(stack)) {
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
        List<FluidBlockTransformRecipe> fluidBlockTransformRecipes = new ArrayList<>();

        for (FluidBlockTransformer transformer : registry) {
            // Make sure everything's registered
            if (FluidRegistry.isFluidRegistered(transformer.getFluidName())
                    && transformer.getInput().getMatchingStacks().length > 0
                    && transformer.getOutput().isValid()) {
                FluidBlockTransformRecipe recipe = new FluidBlockTransformRecipe(transformer);
                if (recipe.isValid()) {
                    fluidBlockTransformRecipes.add(recipe);
                }
            }
        }

        return fluidBlockTransformRecipes;
    }
}
