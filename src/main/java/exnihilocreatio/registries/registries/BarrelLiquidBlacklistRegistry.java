package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BarrelLiquidBlacklistRegistry extends BaseRegistryMap<Integer, List<String>> {
    public BarrelLiquidBlacklistRegistry() {
        super(new GsonBuilder().setPrettyPrinting().create(), ExNihiloRegistryManager.BARREL_LIQUID_BLACKLIST_DEFAULT_REGISTRY_PROVIDERS);
    }

    @SuppressWarnings("unchecked")
    public boolean isBlacklisted(int level, String fluid) {
        return level < 0 || registry.getOrDefault(level, Collections.EMPTY_LIST).contains(fluid);
    }

    public void register(int level, String fluid) {
        List<String> list = registry.computeIfAbsent(level, k -> new ArrayList<>());

        list.add(fluid);
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        Map<Integer, List<String>> loaded = gson.fromJson(fr, new TypeToken<Map<Integer, List<String>>>() {
        }.getType());

        loaded.forEach((integer, strings) -> {
            for (String string : strings) {
                register(integer, string);
            }
        });
    }

    @Override
    public List<?> getRecipeList() {
        return Lists.newLinkedList();
    }
}
