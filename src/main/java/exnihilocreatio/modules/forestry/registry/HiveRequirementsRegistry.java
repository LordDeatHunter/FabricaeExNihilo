package exnihilocreatio.modules.forestry.registry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomIngredientJson;
import exnihilocreatio.modules.Forestry;
import exnihilocreatio.registries.ingredient.OreIngredientStoring;
import exnihilocreatio.registries.manager.IDefaultRecipeProvider;
import exnihilocreatio.registries.registries.prefab.BaseRegistryList;
import exnihilocreatio.registries.types.Compostable;
import exnihilocreatio.util.BlockInfo;
import net.minecraft.item.crafting.Ingredient;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class HiveRequirementsRegistry extends BaseRegistryList<HiveRequirements> {

    public HiveRequirementsRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(HiveRequirements.class, CustomHiveRequirementsJson.INSTANCE)
                        .registerTypeAdapter(BlockInfo.class, CustomBlockInfoJson.INSTANCE)
                        .registerTypeAdapter(Ingredient.class, CustomIngredientJson.INSTANCE)
                        .registerTypeAdapter(OreIngredientStoring.class, CustomIngredientJson.INSTANCE)
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
        List<HiveRequirements> gsonInput = gson.fromJson(fr, new TypeToken<List<HiveRequirements>>() {}.getType());
    }

    @Override
    public List<?> getRecipeList() {
        return registry;
    }
}
