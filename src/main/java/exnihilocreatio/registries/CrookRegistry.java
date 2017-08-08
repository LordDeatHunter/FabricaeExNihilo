package exnihilocreatio.registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomItemStackJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.manager.ICrookDefaultRegistryProvider;
import exnihilocreatio.registries.types.CrookReward;
import exnihilocreatio.util.BlockInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class CrookRegistry {

    private static Map<BlockInfo, List<CrookReward>> registry = new HashMap<>();
    private static Map<BlockInfo, List<CrookReward>> externalRegistry = new HashMap<>();
    private static Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ItemStack.class, new CustomItemStackJson()).registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson()).create();

    public static void registerDefaults() {
        for (ICrookDefaultRegistryProvider iCrookDefaultRegistryProvider : ExNihiloRegistryManager.getDefaultCrookRecipeHandlers()) {
            iCrookDefaultRegistryProvider.registerCrookRecipeDefaults();
        }
    }

    public static void register(BlockInfo info, ItemStack reward, float chance, float fortuneChance) {
        registerInternal(info, reward, chance, fortuneChance);

        List<CrookReward> list = externalRegistry.get(info);

        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(new CrookReward(reward, chance, fortuneChance));
        externalRegistry.put(info, list);
    }

    private static void registerInternal(BlockInfo info, ItemStack reward, float chance, float fortuneChance) {
        List<CrookReward> list = registry.get(info);

        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(new CrookReward(reward, chance, fortuneChance));
        registry.put(info, list);
    }

    public static boolean registered(Block block) {
        return registry.containsKey(new BlockInfo(block.getDefaultState()));
    }

    public static ArrayList<CrookReward> getRewards(IBlockState state) {
        BlockInfo info = new BlockInfo(state);
        if (!registry.containsKey(info))
            return null;

        return (ArrayList<CrookReward>) registry.get(info);
    }

    public static void loadJson(File file) {
        registry.clear();

        if (file.exists()) {
            try {
                FileReader fr = new FileReader(file);
                HashMap<String, ArrayList<CrookReward>> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, ArrayList<CrookReward>>>() {
                }.getType());

                Iterator<String> it = gsonInput.keySet().iterator();

                while (it.hasNext()) {
                    String s = (String) it.next();
                    BlockInfo stack = new BlockInfo(s);
                    registry.put(stack, gsonInput.get(s));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            registerDefaults();
            saveJson(file);
        }
    }

    public static void saveJson(File file) {
        try {
            FileWriter fw = new FileWriter(file);

            gson.toJson(registry, fw);

            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
