package wraith.fabricaeexnihilo.api;

import net.minecraft.block.Block;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.ApiStatus;
import wraith.fabricaeexnihilo.util.Color;
import wraith.fabricaeexnihilo.util.Lazy;

import java.util.Arrays;

/**
 * Interface that contains methods that allow registering content for FEN internals. All methods are free to be called. Do not extend!
 * @see FENApiModule
 */
@ApiStatus.NonExtendable
public interface FENRegistries {
    /**
     * Register a new type of ore piece. It will be registered under {@code fabricaeexnihilo:<name>_piece}. You will have to manually provide all assets. (layer1 will be tinted based on {@code color}).
     * @param name The name of the ore.
     * @param color The color of the overlay.
     */
    void registerOrePiece(String name, Color color);
    
    /**
     * Register a new type of mesh piece. It will be registered under {@code fabricaeexnihilo:<name>_mesh}. You will have to manually provide all assets. The model {@code fabricaeexnihilo:item/mesh} can be directly extended. (tintindex 1 will be tinted based on {@code color}).
     * @param name The name of the mesh.
     * @param color The color it will be tinted.
     * @param enchantability The enchantability of the mesh. You can use vanilla tool materials as a reference.
     */
    void registerMesh(String name, Color color, int enchantability);
    
    /**
     * Register a new crushed block. It will be registered under {@code fabricaeexnihilo:crushed_<name>}. You will have to manually provide all assets.
     * @param name The base name of the crushed block.
     * @param isSandy Whether it should be considered sandy. Affects the sounds of the block.
     */
    void registerCrushedBlock(String name, boolean isSandy);
    
    /**
     * Registers a new type of wood block by calling all the relevant methods. You will need an additional call to {@link #registerInfestedLeaves} and {@link #registerSeed} to register the corresponding leaves and seed respectively.
     * @param name The name of the wood type. Will be directly passed into the called methods.
     * @see #registerBarrel
     * @see #registerCrucible
     * @see #registerSieve
     * @see #registerStrainer
     */
    default void registerWood(String name) {
        registerSieve(name);
        registerStrainer(name);
        registerBarrel(name, false);
        registerCrucible(name, false);
    }
    
    /**
     * Register a new type of sieve. It will be registered under {@code fabricaeexnihilo:<name>_sieve}. You will have to manually provide all assets. The model {@code fabricaeexnihilo:block/sieve} can be extended for convenience.
     * @param name The name of the sieve.
     */
    void registerSieve(String name);
    
    /**
     * Register a new type of strainer. It will be registered under {@code fabricaeexnihilo:<name>_strainer}. You will have to manually provide all assets. The model {@code fabricaeexnihilo:block/strainer} can be extended for convenience.
     * @param name The name of the strainer.
     */
    void registerStrainer(String name);
    
    /**
     * Register a new type of crucible. It will be registered under {@code fabricaeexnihilo:<name>_crucible}. You will have to manually provide all assets. The model {@code fabricaeexnihilo:block/crucible} can be extended for convenience.
     * @param name The name of the crucible.
     * @param isStone Whether it should be considered made of stone. Affects the sounds of the block.
     */
    void registerCrucible(String name, boolean isStone);
    
    /**
     * Register a new type of barrel. It will be registered under {@code fabricaeexnihilo:<name>_barrel}. You will have to manually provide all assets. The model {@code fabricaeexnihilo:block/barrel} can be extended for convenience.
     * @param name The name of the barrel.
     * @param isStone Whether it should be considered made of stone. Affects the sounds of the block.
     */
    void registerBarrel(String name, boolean isStone);
    
    /**
     * Register a new type of infested leaves. It will be registered under {@code fabricaeexnihilo:infested_<name>_leaves}. You will have to manually provide all assets, but you can reuse the standard leaves model. The tintindex of 0 is provided as white.
     * @param name The name of the wood type.
     * @param source The source of the transformation. Used in code.
     */
    void registerInfestedLeaves(String name, Identifier source);
    
    /**
     * Register a new type of seed that transforms one block into another. It will be registered under {@code fabricaeexnihilo:<name>_seeds}. You will have to manually provide all assets.
     * @param name The name of the seeds.
     * @param from A lazy that resolves to the source block.
     * @param to A lazy that resolves to the result block.
     * @see #registerTransformingSeed(String, Identifier, Identifier)
     */
    void registerTransformingSeed(String name, Lazy<Block> from, Lazy<Block> to);
    
    /**
     * Register a new type of seed that transforms one block into another. It will be registered under {@code fabricaeexnihilo:<name>_seeds}. You will have to manually provide all assets.
     * @param name The name of the seeds.
     * @param from An id that points to the source block.
     * @param to An id that points to the result block.
     * @see #registerTransformingSeed(String, Lazy, Lazy)
     */
    default void registerTransformingSeed(String name, Identifier from, Identifier to) {
        registerTransformingSeed(name, new Lazy<>(() -> Registry.BLOCK.get(from)), new Lazy<>(() -> Registry.BLOCK.get(to)));
    }
    
    /**
     * Register a new type of seed that creates tall plants. It will be registered under {@code fabricaeexnihilo:<name>_seeds}. You will have to manually provide all assets.
     * @param name The name of the seeds.
     * @param plants A lazy that resolves to the possible results.
     * @see #registerTallPlantSeed(String, Identifier...)
     */
    void registerTallPlantSeed(String name, Lazy<TallPlantBlock[]> plants);
    
    /**
     * Register a new type of seed that creates tall plants. It will be registered under {@code fabricaeexnihilo:<name>_seeds}. You will have to manually provide all assets.
     * @param name The name of the seeds.
     * @param plants Ids that resolve to possible results.
     * @see #registerTallPlantSeed(String, Identifier...)
     */
    default void registerTallPlantSeed(String name, Identifier... plants) {
        registerTallPlantSeed(name, new Lazy<>(() -> Arrays.stream(plants)
                .map(Registry.BLOCK::get)
                .map(block -> (TallPlantBlock) block)
                .toArray(TallPlantBlock[]::new)));
    }
    
    /**
     * Register a new type of seed that creates normal plants. It will be registered under {@code fabricaeexnihilo:<name>_seeds}. You will have to manually provide all assets.
     * @param name The name of the seeds.
     * @param plants A lazy that resolves to the possible results.
     * @see #registerSeed(String, Identifier...)
     */
    void registerSeed(String name, Lazy<Block[]> plants);
    
    /**
     * Register a new type of seed that creates normal plants. It will be registered under {@code fabricaeexnihilo:<name>_seeds}. You will have to manually provide all assets.
     * @param name The name of the seeds.
     * @param plants Ids that resolve to possible results.
     * @see #registerSeed(String, Identifier...)
     */
    default void registerSeed(String name, Identifier... plants) {
        registerSeed(name, new Lazy<>(() -> Arrays.stream(plants)
                .map(Registry.BLOCK::get)
                .toArray(Block[]::new)));
    }

}
