package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.compatibility.jei.crucible.HeatSourcesRecipe;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.util.BlockInfo;
import net.minecraft.item.ItemStack;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeatRegistry extends BaseRegistryMap<BlockInfo, Integer> {

    public HeatRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(BlockInfo.class, CustomBlockInfoJson.INSTANCE)
                        .create(),
                new com.google.gson.reflect.TypeToken<Map<BlockInfo, Integer>>() {}.getType(),
                ExNihiloRegistryManager.HEAT_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(BlockInfo info, int heatAmount) {
        registry.put(info, heatAmount);
    }

    public void register(ItemStack stack, int heatAmount) {
        register(new BlockInfo(stack), heatAmount);
    }

    public int getHeatAmount(ItemStack stack) {
        return registry.get(new BlockInfo(stack));
    }

    public int getHeatAmount(BlockInfo info) {
        return registry.getOrDefault(info, 0);
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        HashMap<String, Integer> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, Integer>>() {
        }.getType());

        for (Map.Entry<String, Integer> entry : gsonInput.entrySet()) {
            registry.put(new BlockInfo(entry.getKey()), entry.getValue());
        }
    }

    @Override
    public List<HeatSourcesRecipe> getRecipeList() {
        List<HeatSourcesRecipe> heatSources = Lists.newLinkedList();
        getRegistry().forEach((key, value) -> heatSources.add(new HeatSourcesRecipe(key, value)));
        return heatSources;
    }
}
