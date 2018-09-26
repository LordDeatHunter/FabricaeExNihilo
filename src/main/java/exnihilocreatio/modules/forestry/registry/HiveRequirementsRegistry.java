package exnihilocreatio.modules.forestry.registry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.modules.Forestry;
import exnihilocreatio.registries.manager.IDefaultRecipeProvider;
import exnihilocreatio.registries.registries.prefab.BaseRegistryList;
import exnihilocreatio.util.BlockInfo;

import java.io.FileReader;
import java.util.List;

public class HiveRequirementsRegistry extends BaseRegistryList<HiveRequirements> {

    public HiveRequirementsRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(BlockInfo.class, CustomBlockInfoJson.INSTANCE)
                        .enableComplexMapKeySerialization()
                        .create(),
                Forestry.HIVE_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public HiveRequirementsRegistry(Gson gson, List<? extends IDefaultRecipeProvider> defaultRecipeProviders) {
        super(gson, defaultRecipeProviders);
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {

    }

    @Override
    public List<?> getRecipeList() {
        return registry;
    }
}
