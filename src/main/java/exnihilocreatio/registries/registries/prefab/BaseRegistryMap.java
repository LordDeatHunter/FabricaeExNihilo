package exnihilocreatio.registries.registries.prefab;

import com.google.gson.Gson;
import exnihilocreatio.registries.manager.IDefaultRecipeProvider;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRegistryMap<K, V> extends BaseRegistry<Map<K, V>> {

    public BaseRegistryMap(Gson gson, List<? extends IDefaultRecipeProvider> defaultRecipeProviders) {
        super(gson, new HashMap<>(), defaultRecipeProviders);
    }

    public void loadJson(File file) {
        if (hasAlreadyBeenLoaded) registry.clear();

        if (file.exists()) {
            try {
                FileReader fr = new FileReader(file);
                registerEntriesFromJSON(fr);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            registerDefaults();
            saveJson(file);
        }

        hasAlreadyBeenLoaded = true;
    }

    public abstract void registerEntriesFromJSON(FileReader fr);

    public void register(K key, V value) {
        registry.put(key, value);
    }
}
