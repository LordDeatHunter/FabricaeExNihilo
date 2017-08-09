package exnihilocreatio.registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.manager.IFluidTransformDefaultRegistryProvider;
import exnihilocreatio.registries.types.FluidTransformer;
import exnihilocreatio.util.BlockInfo;
import lombok.Getter;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FluidTransformRegistry {

    @Getter
    private static ArrayList<FluidTransformer> registry = new ArrayList<>();
    private static List<FluidTransformer> externalRegistry = new ArrayList<>();

    private static HashMap<String, List<FluidTransformer>> registryInternal = new HashMap<>();
    private static Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson()).create();

    public static void register(String inputFluid, String outputFluid, int duration, BlockInfo[] transformingBlocks, BlockInfo[] blocksToSpawn) {
        register(new FluidTransformer(inputFluid, outputFluid, duration, transformingBlocks, blocksToSpawn));
    }

    public static void register(FluidTransformer transformer) {
        registerInternal(transformer);
        externalRegistry.add(transformer);
    }

    private static void registerInternal(String inputFluid, String outputFluid, int duration, BlockInfo[] transformingBlocks, BlockInfo[] blocksToSpawn) {
        registerInternal(new FluidTransformer(inputFluid, outputFluid, duration, transformingBlocks, blocksToSpawn));
    }

    private static void registerInternal(FluidTransformer transformer) {
        registry.add(transformer);

        List<FluidTransformer> list = registryInternal.get(transformer.getInputFluid());

        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(transformer);
        registryInternal.put(transformer.getInputFluid(), list);
    }

    public static boolean containsKey(String inputFluid) {
        return registryInternal.containsKey(inputFluid);
    }

    public static FluidTransformer getFluidTransformer(String inputFluid, String outputFluid) {
        for (FluidTransformer transformer : registry) {
            if (transformer.getInputFluid().equals(inputFluid) && transformer.getOutputFluid().equals(outputFluid))
                return transformer;
        }
        return null;
    }

    public static ArrayList<FluidTransformer> getFluidTransformers(String inputFluid) {
        return (ArrayList<FluidTransformer>) registryInternal.get(inputFluid);
    }

    public static void registerDefaults() {
        for (IFluidTransformDefaultRegistryProvider provider : ExNihiloRegistryManager.getDefaultFluidTransformRecipeHandlers()) {
            provider.registerFluidTransformRecipeDefaults();
        }
    }

    public static void loadJson(File file) {
        registry.clear();
        registryInternal.clear();

        if (file.exists()) {
            try {
                FileReader fr = new FileReader(file);
                List<FluidTransformer> gsonInput = gson.fromJson(fr, new TypeToken<List<FluidTransformer>>() {
                }.getType());

                for (FluidTransformer transformer : gsonInput) {
                    registerInternal(transformer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            registerDefaults();
            saveJson(file);
        }

        for (FluidTransformer transformer : externalRegistry) {
            registerInternal(transformer);
        }
    }

    public static void saveJson(File file) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            gson.toJson(registry, fw);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fw);
        }
    }
}
