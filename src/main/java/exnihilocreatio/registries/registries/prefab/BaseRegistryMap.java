package exnihilocreatio.registries.registries.prefab;

import com.google.gson.Gson;
import exnihilocreatio.registries.manager.IDefaultRecipeProvider;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRegistryMap<K, V> extends BaseRegistry<Map<K, V>> {

    public BaseRegistryMap(Gson gson, Type typeOfSource, List<? extends IDefaultRecipeProvider> defaultRecipeProviders) {
        super(gson, new HashMap<>(), typeOfSource,   defaultRecipeProviders);
    }

    public void register(K key, V value) {
        registry.put(key, value);
    }

    @Override
    public void clearRegistry() {
        registry.clear();
    }

}
