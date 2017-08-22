package exnihilocreatio.registries.registries.prefab;

import com.google.gson.Gson;
import exnihilocreatio.registries.manager.IDefaultRecipeProvider;
import lombok.Getter;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public abstract class BaseRegistry<RegType> {
    protected boolean hasAlreadyBeenLoaded = false;

    @Getter
    protected RegType registry;
    protected List<? extends IDefaultRecipeProvider> defaultRecipeProviders;

    protected Gson gson;

    public BaseRegistry(Gson gson, RegType registry, List<? extends IDefaultRecipeProvider> defaultRecipeProviders) {
        this.gson = gson;
        this.registry = registry;
        this.defaultRecipeProviders = defaultRecipeProviders;
    }

    public void saveJson(File file) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);

            gson.toJson(registry, fw);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fw);
        }
    }

    public void loadJson(File file) {
        if (hasAlreadyBeenLoaded) clearRegistry();

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

    protected abstract void registerEntriesFromJSON(FileReader fr);

    public void registerDefaults() {
        if (defaultRecipeProviders != null) {
            for (IDefaultRecipeProvider defaultRecipeProvider : defaultRecipeProviders) {
                if (defaultRecipeProvider != null) {
                    defaultRecipeProvider.registerRecipeDefaults(this);
                }
            }
        }
    }

    protected abstract void clearRegistry();
}
