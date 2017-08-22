package exnihilocreatio.registries.registries.prefab;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.registries.manager.IDefaultRecipeProvider;

import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.List;

//TODO: Copy stuff from the map variant
public abstract class BaseRegistryCollection<RegType extends Collection> extends BaseRegistry<RegType> {

    public BaseRegistryCollection(Gson gson, RegType registry, List<? extends IDefaultRecipeProvider> defaultRecipeProviders) {
        super(gson, registry, defaultRecipeProviders);
    }

    public void loadJson(File file) {
        registry.clear();

        if (file.exists()) {
            try {
                FileReader fr = new FileReader(file);
                RegType gsonInput = gson.fromJson(fr, new TypeToken<RegType>() {
                }.getType());

                registry.addAll(gsonInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            registerDefaults();
            saveJson(file);
        }

        // registry.addAll(externalRegistry);
    }

}
