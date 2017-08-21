package exnihilocreatio.registries.registries.prefab;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.util.Collection;

public abstract class BaseRegistryCollection<RegType extends Collection>  extends BaseRegistry<RegType>{

    public BaseRegistryCollection(Gson gson, RegType registry) {
        super(gson, registry);
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
