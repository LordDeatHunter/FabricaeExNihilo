package exnihilocreatio.registries.registries.prefab;

import com.google.gson.Gson;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.registries.manager.IDefaultRecipeProvider;
import lombok.Getter;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;

public abstract class BaseRegistry<RegType> {
    protected final Gson gson;
    private final List<? extends IDefaultRecipeProvider> defaultRecipeProviders;
    protected boolean hasAlreadyBeenLoaded = false;
    @Getter
    protected RegType registry;

    @Getter
    protected Type typeOfSource;

    public BaseRegistry(Gson gson, RegType registry, Type typeOfSource, @Nonnull List<? extends IDefaultRecipeProvider> defaultRecipeProviders) {
        this.gson = gson;
        this.registry = registry;
        this.typeOfSource = typeOfSource;
        this.defaultRecipeProviders = defaultRecipeProviders;
    }

    public void saveJson(File file) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            // TODO remove null again
            if (typeOfSource != null) {
                gson.toJson(registry, typeOfSource, fw);
            } else {
                gson.toJson(registry, fw);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fw);
        }
    }

    public void loadJson(File file) {
        if (hasAlreadyBeenLoaded) clearRegistry();

        if (file.exists() && ModConfig.misc.enableJSONLoading) {
            try {
                FileReader fr = new FileReader(file);
                registerEntriesFromJSON(fr);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            registerDefaults();
            if (ModConfig.misc.enableJSONLoading) {
                saveJson(file);
            }
        }

        hasAlreadyBeenLoaded = true;
    }

    protected abstract void registerEntriesFromJSON(FileReader fr);

    @SuppressWarnings("unchecked")
    public void registerDefaults() {
        defaultRecipeProviders.forEach(recipeProvider -> recipeProvider.registerRecipeDefaults(this));
    }

    public abstract List<?> getRecipeList();

    public abstract void clearRegistry();
}
