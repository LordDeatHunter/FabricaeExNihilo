package wraith.fabricaeexnihilo.api;

/**
 * An entrypoint that allows mods to add their own resources to Fabricae Ex Nihilo.
 */
public interface FabricaeExNihiloApiModule {
    void register(FENRegistries registries);
    
    /**
     * You can override this if you need to do additional checks before the module is used. Mostly used by integrated compatibility modules to check if the target mod is loaded.
     *
     * @return Whether the module should be loaded.
     */
    default boolean shouldLoad() {
        return true;
    }
}
