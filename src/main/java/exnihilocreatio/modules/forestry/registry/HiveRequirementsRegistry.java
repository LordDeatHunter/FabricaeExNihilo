package exnihilocreatio.modules.forestry.registry;

import com.google.gson.Gson;
import exnihilocreatio.registries.manager.IDefaultRecipeProvider;
import exnihilocreatio.registries.registries.prefab.BaseRegistryList;
import lombok.Getter;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class HiveRequirementsRegistry extends BaseRegistryList<HiveRequirements> {
    @Getter
    private List<HiveRequirements> recipeList = new ArrayList<>();

    public HiveRequirementsRegistry(Gson gson, List<? extends IDefaultRecipeProvider> defaultRecipeProviders) {
        super(gson, defaultRecipeProviders);
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {

    }
}
