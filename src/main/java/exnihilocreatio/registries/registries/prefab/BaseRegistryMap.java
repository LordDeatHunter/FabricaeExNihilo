package exnihilocreatio.registries.registries.prefab;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseRegistryMap<K, V>  extends BaseRegistry<Map<K, V>>{

    public BaseRegistryMap(Gson gson) {
        super(gson, new HashMap<>());
    }

    public void loadJson(File file) {
        registry.clear();

        if (file.exists()) {
            try {
                FileReader fr = new FileReader(file);
                registerEntries(fr);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            registerDefaults();
            saveJson(file);
        }

        // registry.addAll(externalRegistry);
    }

    public abstract void registerEntries(FileReader fr);

    public void register(K key, V value){
        registry.put(key, value);
    }
}
