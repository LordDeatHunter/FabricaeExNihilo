package exnihilocreatio.registries;

import exnihilocreatio.registries.manager.ExNihiloRegistryManager;

/**
 * @deprecated use classes from [{@link exnihilocreatio.api.ExNihiloCreatioAPI}]
 */
@Deprecated
public class BarrelLiquidBlacklistRegistry {

    @SuppressWarnings("unchecked")
    public static boolean isBlacklisted(int level, String fluid) {
        return ExNihiloRegistryManager.BARREL_LIQUID_BLACKLIST_REGISTRY.isBlacklisted(level, fluid);
    }

    public static void register(int level, String fluid) {
        ExNihiloRegistryManager.BARREL_LIQUID_BLACKLIST_REGISTRY.register(level, fluid);
    }
}
