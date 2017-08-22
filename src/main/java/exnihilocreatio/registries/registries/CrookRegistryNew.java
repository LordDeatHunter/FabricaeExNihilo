package exnihilocreatio.registries.registries;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomItemStackJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.CrookReward;
import exnihilocreatio.util.BlockInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrookRegistryNew extends BaseRegistryMap<BlockInfo, List<CrookReward>> {

    public CrookRegistryNew() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemStack.class, new CustomItemStackJson())
                        .registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson())
                        .create(),
                ExNihiloRegistryManager.CROOK_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    @Override
    public void register(BlockInfo key, List<CrookReward> value) {
        List<CrookReward> list = registry.get(key);

        if (list == null) {
            list = new ArrayList<>();
        }

        list.addAll(value);
    }

    public void register(BlockInfo info, ItemStack reward, float chance, float fortuneChance) {
        List<CrookReward> list = registry.get(info);

        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(new CrookReward(reward, chance, fortuneChance));
        registry.put(info, list);
    }

    public boolean isRegistered(Block block) {
        return registry.containsKey(new BlockInfo(block.getDefaultState()));
    }

    public List<CrookReward> getRewards(IBlockState state) {
        BlockInfo info = new BlockInfo(state);
        if (!registry.containsKey(info))
            return null;

        return registry.get(info);
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        HashMap<String, ArrayList<CrookReward>> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, ArrayList<CrookReward>>>() {
        }.getType());

        for (Map.Entry<String, ArrayList<CrookReward>> s : gsonInput.entrySet()) {
            BlockInfo blockInfo = new BlockInfo(s.getKey());
            registry.put(blockInfo, s.getValue());
        }
    }
}
