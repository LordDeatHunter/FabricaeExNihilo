package exnihilocreatio.registries.manager;

import exnihilocreatio.registries.registries.prefab.BaseRegistry;

public interface IDefaultRecipeProvider<T extends BaseRegistry> {

    void registerRecipeDefaults(T registry);

}
