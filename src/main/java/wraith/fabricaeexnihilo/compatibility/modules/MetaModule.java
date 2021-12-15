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

    public void registerAlchemy(@NotNull AlchemyRecipeRegistry registry) {
        modules.forEach(module -> module.registerAlchemy(registry));
    }

    public void registerCompost(@NotNull CompostRecipeRegistry registry) {
        modules.forEach(module -> module.registerCompost(registry));
    }

    public void registerLeaking(@NotNull LeakingRecipeRegistry registry) {
        modules.forEach(module -> module.registerLeaking(registry));
    }

    @Override
    public void registerFluidOnTop(@NotNull FluidOnTopRecipeRegistry registry) {
        modules.forEach(module -> module.registerFluidOnTop(registry));
    }

    @Override
    public void registerFluidTransform(@NotNull FluidTransformRecipeRegistry registry) {
        modules.forEach(module -> module.registerFluidTransform(registry));
    }

    @Override
    public void registerMilking(@NotNull MilkingRecipeRegistry registry) {
        modules.forEach(module -> module.registerMilking(registry));
    }

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
