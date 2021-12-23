package wraith.fabricaeexnihilo.compatibility.modules;

import org.jetbrains.annotations.NotNull;
import wraith.fabricaeexnihilo.api.compatibility.FabricaeExNihiloModule;
import wraith.fabricaeexnihilo.api.registry.*;

import java.util.ArrayList;
import java.util.List;

public class MetaModule implements FabricaeExNihiloModule {

    public static MetaModule INSTANCE = new MetaModule();

    private MetaModule() {}

    public List<FabricaeExNihiloModule> modules = new ArrayList<>();

    @Override
    public void registerCrucibleHeat(@NotNull CrucibleHeatRecipeRegistry registry) {
        modules.forEach(module -> module.registerCrucibleHeat(registry));
    }

    @Override
    public void registerCrucibleStone(@NotNull CrucibleRecipeRegistry registry) {
        modules.forEach(module -> module.registerCrucibleStone(registry));
    }

    @Override
    public void registerCrucibleWood(@NotNull CrucibleRecipeRegistry registry) {
        modules.forEach(module -> module.registerCrucibleWood(registry));
    }

    @Override
    public void registerOres(@NotNull OreRecipeRegistry registry) {
        modules.forEach(module -> module.registerOres(registry));
    }

    @Override
    public void registerSieve(@NotNull SieveRecipeRegistry registry) {
        modules.forEach(module -> module.registerSieve(registry));
    }

    @Override
    public void registerMesh(@NotNull MeshRecipeRegistry registry) {
        modules.forEach(module -> module.registerMesh(registry));
    }

    @Override
    public void registerCrook(@NotNull ToolRecipeRegistry registry) {
        modules.forEach(module -> module.registerCrook(registry));
    }

    @Override
    public void registerHammer(@NotNull ToolRecipeRegistry registry) {
        modules.forEach(module -> module.registerHammer(registry));
    }

    @Override
    public void registerWitchWaterWorld(@NotNull WitchWaterWorldRecipeRegistry registry) {
        modules.forEach(module -> module.registerWitchWaterWorld(registry));
    }

    @Override
    public void registerWitchWaterEntity(@NotNull WitchWaterEntityRecipeRegistry registry) {
        modules.forEach(module -> module.registerWitchWaterEntity(registry));
    }

}
