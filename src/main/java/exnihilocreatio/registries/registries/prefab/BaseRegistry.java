package exnihilocreatio.registries.registries.prefab;

import com.google.gson.Gson;
import lombok.Getter;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;

public abstract class BaseRegistry<RegType> {

    @Getter
    protected RegType registry;

    protected Gson gson;

    public BaseRegistry(Gson gson, RegType registry) {
        this.gson = gson;
        this.registry = registry;
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

    public abstract void loadJson(File file);

    public abstract void registerDefaults();

}
