package exnihilocreatio.registries.registries.prefab;

import com.google.gson.Gson;
import exnihilocreatio.registries.manager.IDefaultRecipeProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRegistryMap<K, V> extends BaseRegistry<Map<K, V>> {

    public BaseRegistryMap(Gson gson, List<? extends IDefaultRecipeProvider> defaultRecipeProviders) {
        super(gson, new HashMap<>(), defaultRecipeProviders);
    }

    public void register(K key, V value) {
        registry.put(key, value);
    }

    @Override
    protected void clearRegistry() {
        registry.clear();
    }
}
