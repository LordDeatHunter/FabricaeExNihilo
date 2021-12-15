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

    public void registerAlchemy(@NotNull AlchemyRegistry registry) {
        modules.forEach(module -> module.registerAlchemy(registry));
    }

    public void registerCompost(@NotNull CompostRegistry registry) {
        modules.forEach(module -> module.registerCompost(registry));
    }

    public void registerLeaking(@NotNull LeakingRegistry registry) {
        modules.forEach(module -> module.registerLeaking(registry));
    }

    @Override
    public void registerFluidOnTop(@NotNull FluidOnTopRegistry registry) {
        modules.forEach(module -> module.registerFluidOnTop(registry));
    }

    @Override
    public void registerFluidTransform(@NotNull FluidTransformRegistry registry) {
        modules.forEach(module -> module.registerFluidTransform(registry));
    }

    @Override
    public void registerMilking(@NotNull MilkingRegistry registry) {
        modules.forEach(module -> module.registerMilking(registry));
    }

    @Override
    public void registerCrucibleHeat(@NotNull CrucibleHeatRegistry registry) {
        modules.forEach(module -> module.registerCrucibleHeat(registry));
    }

    @Override
    public void registerCrucibleStone(@NotNull CrucibleRegistry registry) {
        modules.forEach(module -> module.registerCrucibleStone(registry));
    }

    @Override
    public void registerCrucibleWood(@NotNull CrucibleRegistry registry) {
        modules.forEach(module -> module.registerCrucibleWood(registry));
    }

    @Override
    public void registerOres(@NotNull OreRegistry registry) {
        modules.forEach(module -> module.registerOres(registry));
    }

    @Override
    public void registerSieve(@NotNull SieveRegistry registry) {
        modules.forEach(module -> module.registerSieve(registry));
    }

    @Override
    public void registerMesh(@NotNull MeshRegistry registry) {
        modules.forEach(module -> module.registerMesh(registry));
    }

    @Override
    public void registerCrook(@NotNull ToolRegistry registry) {
        modules.forEach(module -> module.registerCrook(registry));
    }

    @Override
    public void registerHammer(@NotNull ToolRegistry registry) {
        modules.forEach(module -> module.registerHammer(registry));
    }

    @Override
    public void registerWitchWaterWorld(@NotNull WitchWaterWorldRegistry registry) {
        modules.forEach(module -> module.registerWitchWaterWorld(registry));
    }

    @Override
    public void registerWitchWaterEntity(@NotNull WitchWaterEntityRegistry registry) {
        modules.forEach(module -> module.registerWitchWaterEntity(registry));
    }

}
