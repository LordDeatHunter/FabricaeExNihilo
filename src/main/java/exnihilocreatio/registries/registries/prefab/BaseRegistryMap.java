package exnihilocreatio.registries.registries.prefab;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.Map;

public abstract class BaseRegistryMap<RegType extends Map>  extends BaseRegistry<RegType>{

    public BaseRegistryMap(Gson gson, RegType registry) {
        super(gson, registry);
    }

    public void loadJson(File file) {
        registry.clear();

        if (file.exists()) {
            try {
                FileReader fr = new FileReader(file);
                RegType gsonInput = gson.fromJson(fr, new TypeToken<RegType>() {}.getType());

                registry.putAll(gsonInput);
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
