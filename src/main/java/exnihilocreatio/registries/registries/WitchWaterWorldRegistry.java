package exnihilocreatio.registries.registries;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.api.registries.IWitchWaterWorldRegistry;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomWitchWaterWorld;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.WitchWaterWorld;
import exnihilocreatio.util.BlockInfo;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WitchWaterWorldRegistry extends BaseRegistryMap<Fluid, WitchWaterWorld> implements IWitchWaterWorldRegistry {

    public WitchWaterWorldRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(BlockInfo.class, CustomBlockInfoJson.INSTANCE)
                        .registerTypeAdapter(WitchWaterWorld.class, CustomWitchWaterWorld.INSTANCE)
                        .enableComplexMapKeySerialization()
                        .create(),
                new com.google.gson.reflect.TypeToken<Map<String, WitchWaterWorld>>() {}.getType(),
                ExNihiloRegistryManager.WITCH_WATER_WORLD_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    @Override
    public void register(Fluid key, WitchWaterWorld value) {
        registry.put(key, value);
    }

    public void register(Fluid key, Map<BlockInfo, Integer> entries) {
        register(key, WitchWaterWorld.fromMap(entries));
    }

    public boolean contains(@NotNull Fluid fluid) {
        return registry.containsKey(fluid);
    }

    @NotNull
    @Override
    public BlockInfo getResult(@NotNull Fluid fluid, float chance) {
        if(registry.containsKey(fluid))
            return registry.get(fluid).getResult(chance);
        return BlockInfo.EMPTY;
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        Map<String, WitchWaterWorld> gsonInput = gson.fromJson(fr, new TypeToken<Map<String, WitchWaterWorld>>() {}.getType());
        for(Map.Entry<String, WitchWaterWorld> entry: gsonInput.entrySet())
            register(FluidRegistry.getFluid(entry.getKey()), entry.getValue());
    }


    @Override
    public void saveJson(File file) {
        Map<String, WitchWaterWorld> mappedRegistry = new HashMap<>();

        for(Map.Entry<Fluid, WitchWaterWorld> entry : registry.entrySet())
            mappedRegistry.put(entry.getKey().getName(), entry.getValue());

        try(FileWriter fw = new FileWriter(file)) {
            if (typeOfSource != null) {
                gson.toJson(mappedRegistry, typeOfSource, fw);
            } else {
                gson.toJson(mappedRegistry, fw);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<?> getRecipeList() {
        return null;
    }

    @Override
    public void clearRegistry() {
        registry.clear();
    }
}
