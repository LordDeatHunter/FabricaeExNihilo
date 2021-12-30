package wraith.fabricaeexnihilo.api.newapi;

import wraith.fabricaeexnihilo.api.newapi.ores.OreDefinition;

import java.util.function.BiConsumer;

/**
 * An entrypoint that allows mods to add their own resources to Fabricae Ex Nihilo.
 */
public interface FabricaeExNihiloApiModule {
    /**
     * Allows the module to register ores for which chunks and pisces will be added. You need to manually add recipes for them. They should be registered with a simple name. For example: {@code tin}
     * @param registry A consumer which accepts the simple name and a definition of the ores properties.
     */
    default void registerOres(BiConsumer<String, OreDefinition> registry) {}
    
    /**
     * You can override this if you need to do additional checks before the module is used. Mostly used by integrated compatibility modules to check if the target mod is loaded.
     * @return Whether the module should be loaded.
     */
    default boolean shouldLoad() {
        return true;
    }
}
