package exnihilocreatio.registries.registries.prefab;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.registries.manager.IDefaultRecipeProvider;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRegistryList<V> extends BaseRegistry<List<V>> {

    public BaseRegistryList(Gson gson, List<? extends IDefaultRecipeProvider> defaultRecipeProviders) {
        super(gson, new ArrayList<>(), defaultRecipeProviders);
    }

    // /** This is not possible due to GSON not liking having generics
    //  * Defaults to just adding everything to the registry
    //  * @param fr FileReader required for the gson reader
    //  */
    // @Override
    // protected void registerEntriesFromJSON(FileReader fr) {
    //     List<V> gsonInput = gson.fromJson(fr, new TypeToken<List<V>>() {
    //     }.getType());
    //     registry.addAll(gsonInput);
    // }

    @Override
    protected void clearRegistry() {
        registry.clear();
    }

    public void register(V value) {
        registry.add(value);
    }
}
