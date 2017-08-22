package exnihilocreatio.registries.registries.prefab;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.registries.manager.IDefaultRecipeProvider;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRegistryList<V> extends BaseRegistry<List<V>> {

    public BaseRegistryList(Gson gson, List<? extends IDefaultRecipeProvider> defaultRecipeProviders) {
        super(gson, new ArrayList<>(), defaultRecipeProviders);
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

    protected void registerEntriesFromJSON(FileReader fr) {
        List<V> gsonInput = gson.fromJson(fr, new TypeToken<V>() {
        }.getType());
        registry.addAll(gsonInput);
    }

    public void register(V value) {
        registry.add(value);
    }
}
