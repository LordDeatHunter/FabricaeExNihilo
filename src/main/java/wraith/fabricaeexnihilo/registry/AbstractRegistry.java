package wraith.fabricaeexnihilo.registry;

import com.google.gson.reflect.TypeToken;
import wraith.fabricaeexnihilo.FabricaeExNihilo;

import java.io.File;
import java.io.FileWriter;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractRegistry<T> {
    protected abstract void registerJson(File file);

    protected abstract T serializable();

    public static <T, U extends AbstractRegistry<T>> U fromJson(File file, Supplier<U> factory, Consumer<U> defaults) {
        var registry = factory.get();
        if (file.exists() && FabricaeExNihilo.CONFIG.useJsonRecipes) {
            registry.registerJson(file);
        } else {
            defaults.accept(registry);
            if (FabricaeExNihilo.CONFIG.useJsonRecipes) {
                try {
                    var fw = new FileWriter(file);
                    FabricaeExNihilo.RECIPE_GSON.toJson(registry.serializable(), TypeToken.class, fw);
                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return registry;
    }

}
