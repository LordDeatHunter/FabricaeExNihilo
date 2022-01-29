package wraith.fabricaeexnihilo.api;

import org.jetbrains.annotations.ApiStatus;

/**
 * An entrypoint that allows mods to add their own resources to Fabricae Ex Nihilo.
 */
@FunctionalInterface
@ApiStatus.OverrideOnly
public interface FENApiModule {
    /**
     * The main entrypoint for FEN plugins.
     * @param registries An object with methods that allows registering to internal registries.
     */
    void onInit(FENRegistries registries);
    
    /**
     * You can override this if you need to do additional checks before the module is used. Mostly used by integrated compatibility modules to check if the target mod is loaded.
     *
     * @return Whether the module should be loaded.
     */
    default boolean shouldLoad() {
        return true;
    }
}
