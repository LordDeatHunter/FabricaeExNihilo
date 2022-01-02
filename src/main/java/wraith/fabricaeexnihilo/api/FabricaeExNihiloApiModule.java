package wraith.fabricaeexnihilo.api;

import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * An entrypoint that allows mods to add their own resources to Fabricae Ex Nihilo.
 */
public interface FabricaeExNihiloApiModule {
    /**
     * Allows the module to register ores for which chunks and pisces will be added. You need to manually add recipes for them. They should be registered with a simple name. For example: {@code tin}
     * @param registry A consumer which accepts the simple name and a definition of the ores properties.
     */
    default void registerOres(BiConsumer<String, OreDefinition> registry) {}
    
    // TODO: Javadoc
    default void registerMeshes(BiConsumer<Identifier, MeshDefinition> registry) {}
    
    /**
     * Allows the module to register wood types that can be used for sieves, barrels and crucibles.
     * @implNote Not called directly, only used by other methods in the default implementation of the api.
     * @param registry A consumer which accepts id under which the wooden blocks will be registered after a suffix has been appended
     */
    default void registerWoods(Consumer<Identifier> registry) {}
    
    /**
     * Allows for finer control over sieves then {@link #registerWoods}. By default, calls {@link #registerWoods} with the registry and appends {@code _sieve} to the id.
     * @param registry A consumer which accepts the ids under which the sieves are to be registered.
     */
    default void registerSieves(Consumer<Identifier> registry) {
        registerWoods(id -> registry.accept(new Identifier(id.getNamespace(), id.getPath() + "_sieve")));
    }
    
    /**
     * Allows for finer control over crucibles then {@link #registerWoods(Consumer)}. By default, calls {@link #registerWoods} with the registry and appends {@code _crucible} to the id.
     * @param registry A consumer which accepts the ids under which the crucibles are to be registered.
     */
    default void registerCrucibles(Consumer<Identifier> registry) {
        registerWoods(id -> registry.accept(new Identifier(id.getNamespace(), id.getPath() + "_crucible")));
    }
    
    
    /**
     * Allows for finer control over barrels then {@link #registerWoods(Consumer)}. By default, calls {@link #registerWoods} with the registry  and appends {@code _barrel} to the id.
     * @param registry A consumer which accepts the ids under which the barrels are to be registered.
     */
    default void registerBarrels(Consumer<Identifier> registry) {
        registerWoods(id -> registry.accept(new Identifier(id.getNamespace(), id.getPath() + "_barrel")));
    }
    
    /**
     * You can override this if you need to do additional checks before the module is used. Mostly used by integrated compatibility modules to check if the target mod is loaded.
     * @return Whether the module should be loaded.
     */
    default boolean shouldLoad() {
        return true;
    }
}
