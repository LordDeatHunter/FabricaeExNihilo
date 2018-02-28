package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.compatibility.jei.barrel.fluidtransform.FluidTransformRecipe;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.FluidTransformer;
import exnihilocreatio.util.BlockInfo;
import net.minecraftforge.fluids.FluidRegistry;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FluidTransformRegistry extends BaseRegistryMap<String, List<FluidTransformer>> {
    public FluidTransformRegistry() {
        super(new GsonBuilder().setPrettyPrinting().registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson()).create(),
                ExNihiloRegistryManager.FLUID_TRANSFORM_DEFAULT_REGISTRY_PROVIDERS);
    }

    public void register(String inputFluid, String outputFluid, int duration, BlockInfo[] transformingBlocks, BlockInfo[] blocksToSpawn) {
        register(new FluidTransformer(inputFluid, outputFluid, duration, transformingBlocks, blocksToSpawn));
    }

    public void register(FluidTransformer transformer) {
        List<FluidTransformer> list = registry.get(transformer.getInputFluid());

        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(transformer);
        registry.put(transformer.getInputFluid(), list);
    }

    public boolean containsKey(String inputFluid) {
        return registry.containsKey(inputFluid);
    }

    public FluidTransformer getFluidTransformer(String inputFluid, String outputFluid) {
        if (registry.containsKey(inputFluid)) {
            for (FluidTransformer transformer : registry.get(inputFluid)) {
                if (transformer.getInputFluid().equals(inputFluid) && transformer.getOutputFluid().equals(outputFluid))
                    return transformer;
            }
        }
        return null;
    }

    public List<FluidTransformer> getFluidTransformers(String inputFluid) {
        return registry.get(inputFluid);
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        List<FluidTransformer> gsonInput = gson.fromJson(fr, new TypeToken<List<FluidTransformer>>() {
        }.getType());

        for (FluidTransformer transformer : gsonInput) {
            register(transformer);
        }
    }

    /**
     * Overridden as I don't want the registry to get saved directly,
     * rather a List that equals the contents of the registry
     */
    @Override
    public void saveJson(File file) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);

            gson.toJson(getFluidTransformers(), fw);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fw);
        }
    }

    public List<FluidTransformer> getFluidTransformers() {
        List<FluidTransformer> fluidTransformers = new ArrayList<>();
        for (List<FluidTransformer> transformers : registry.values()) {
            fluidTransformers.addAll(transformers);
        }
        return fluidTransformers;
    }

    @Override
    public List<FluidTransformRecipe> getRecipeList() {
        List<FluidTransformRecipe> fluidTransformRecipes = Lists.newLinkedList();
        getFluidTransformers().forEach(transformer -> {
            // Make sure both fluids are registered
            if (FluidRegistry.isFluidRegistered(transformer.getInputFluid()) && FluidRegistry.isFluidRegistered(transformer.getOutputFluid())) {
                FluidTransformRecipe recipe = new FluidTransformRecipe(transformer);
                if (recipe.isValid()) {
                    fluidTransformRecipes.add(new FluidTransformRecipe(transformer));
                }
            }
        });
        return fluidTransformRecipes;
    }
}
