package exnihilocreatio.registries.registries;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.util.BlockInfo;
import net.minecraft.item.ItemStack;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class HeatRegistryNew extends BaseRegistryMap<BlockInfo, Integer> {

    public HeatRegistryNew() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson())
                        .create(),
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
        if (registry.containsKey(info))
            return registry.get(info);

        return 0;
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        HashMap<String, Integer> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, Integer>>() {
        }.getType());

        for (Map.Entry<String, Integer> entry : gsonInput.entrySet()) {
            registry.put(new BlockInfo(entry.getKey()), entry.getValue());
        }
    }
}
