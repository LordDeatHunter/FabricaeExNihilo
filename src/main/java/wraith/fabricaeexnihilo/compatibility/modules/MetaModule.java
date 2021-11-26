package wraith.fabricaeexnihilo.compatibility.modules;

import org.jetbrains.annotations.NotNull;
import wraith.fabricaeexnihilo.api.compatibility.IFabricaeExNihiloModule;
import wraith.fabricaeexnihilo.api.registry.*;

import java.util.ArrayList;
import java.util.List;

public class MetaModule implements IFabricaeExNihiloModule {

    public static MetaModule INSTANCE = new MetaModule();

    private MetaModule() {}

    public List<IFabricaeExNihiloModule> modules = new ArrayList<>();

    public void registerAlchemy(@NotNull IAlchemyRegistry registry) {
        modules.forEach(module -> module.registerAlchemy(registry));
    }

    public void registerCompost(@NotNull ICompostRegistry registry) {
        modules.forEach(module -> module.registerCompost(registry));
    }

    public void registerLeaking(@NotNull ILeakingRegistry registry) {
        modules.forEach(module -> module.registerLeaking(registry));
    }

    @Override
    public void registerFluidOnTop(@NotNull IFluidOnTopRegistry registry) {
        modules.forEach(module -> module.registerFluidOnTop(registry));
    }

    @Override
    public void registerFluidTransform(@NotNull IFluidTransformRegistry registry) {
        modules.forEach(module -> module.registerFluidTransform(registry));
    }

    @Override
    public void registerMilking(@NotNull IMilkingRegistry registry) {
        modules.forEach(module -> module.registerMilking(registry));
    }

    @Override
    public void registerCrucibleHeat(@NotNull ICrucibleHeatRegistry registry) {
        modules.forEach(module -> module.registerCrucibleHeat(registry));
    }

    @Override
    public void registerCrucibleStone(@NotNull ICrucibleRegistry registry) {
        modules.forEach(module -> module.registerCrucibleStone(registry));
    }

    @Override
    public void registerCrucibleWood(@NotNull ICrucibleRegistry registry) {
        modules.forEach(module -> module.registerCrucibleWood(registry));
    }

    @Override
    public void registerOres(@NotNull IOreRegistry registry) {
        modules.forEach(module -> module.registerOres(registry));
    }

    @Override
    public void registerSieve(@NotNull ISieveRegistry registry) {
        modules.forEach(module -> module.registerSieve(registry));
    }

    @Override
    public void registerMesh(@NotNull IMeshRegistry registry) {
        modules.forEach(module -> module.registerMesh(registry));
    }

    @Override
    public void registerCrook(@NotNull IToolRegistry registry) {
        modules.forEach(module -> module.registerCrook(registry));
    }

    @Override
    public void registerHammer(@NotNull IToolRegistry registry) {
        modules.forEach(module -> module.registerHammer(registry));
    }

    @Override
    public void registerWitchWaterWorld(@NotNull IWitchWaterWorldRegistry registry) {
        modules.forEach(module -> module.registerWitchWaterWorld(registry));
    }

    @Override
    public void registerWitchWaterEntity(@NotNull IWitchWaterEntityRegistry registry) {
        modules.forEach(module -> module.registerWitchWaterEntity(registry));
    }

}
