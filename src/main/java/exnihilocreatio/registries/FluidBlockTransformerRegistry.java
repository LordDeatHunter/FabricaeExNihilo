package exnihilocreatio.registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.manager.IFluidBlockDefaultRegistryProvider;
import exnihilocreatio.registries.types.FluidBlockTransformer;
import exnihilocreatio.util.ItemInfo;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FluidBlockTransformerRegistry {
    @Getter
    private static ArrayList<FluidBlockTransformer> registry = new ArrayList<>();

    private static List<FluidBlockTransformer> externalRegistry = new ArrayList<>();
    private static Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson()).create();

    public static void register(Fluid fluid, ItemInfo inputBlock, ItemInfo outputBlock) {
        registerInternal(fluid, inputBlock, outputBlock);

        externalRegistry.add(new FluidBlockTransformer(fluid.getName(), inputBlock, outputBlock));
    }

    private static void registerInternal(Fluid fluid, ItemInfo inputBlock, ItemInfo outputBlock) {
        registry.add(new FluidBlockTransformer(fluid.getName(), inputBlock, outputBlock));
    }

    private static void registerInternal(String fluid, ItemInfo inputBlock, ItemInfo outputBlock) {
        registry.add(new FluidBlockTransformer(fluid, inputBlock, outputBlock));
    }

    public static boolean canBlockBeTransformedWithThisFluid(Fluid fluid, ItemStack stack) {
        ItemInfo info = ItemInfo.getItemInfoFromStack(stack);

        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && info.equals(transformer.getInput()))
                return true;
        }
        return false;
    }

    public static ItemInfo getBlockForTransformation(Fluid fluid, ItemStack stack) {
        ItemInfo info = ItemInfo.getItemInfoFromStack(stack);

        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && info.equals(transformer.getInput())) {
                return transformer.getOutput();
            }
        }

        return null;
    }

    public static void loadJson(File file) {
        registry.clear();

        if (file.exists()) {
            try {
                FileReader fr = new FileReader(file);
                List<FluidBlockTransformer> gsonInput = gson.fromJson(fr, new TypeToken<List<FluidBlockTransformer>>() {
                }.getType());

                registry.addAll(gsonInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            registerDefaults();
            saveJson(file);
        }

        registry.addAll(externalRegistry);
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

    public static void registerDefaults() {
        for (IFluidBlockDefaultRegistryProvider provider : ExNihiloRegistryManager.getDefaultFluidBlockRecipeHandlers()) {
            provider.registerFluidBlockRecipeDefaults();
        }
    }
}
