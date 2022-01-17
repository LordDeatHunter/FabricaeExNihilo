package wraith.fabricaeexnihilo.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.api.ore.OreMaterial;
import wraith.fabricaeexnihilo.api.ore.OreShape;
import wraith.fabricaeexnihilo.util.Lazy;
import wraith.fabricaeexnihilo.util.Color;

import java.util.Arrays;
import java.util.function.Predicate;

//TODO: Document
public interface FENRegistries {

    void registerOre(String name, Color color, OreShape oreShape, OreMaterial baseMaterial);

    void registerMesh(String name, Color color, int enchantability);

    void registerSandyCrushed(String name);

    void registerGravelyCrushed(String name);

    default void registerWood(String name) {
        registerSieve(name);
        registerBarrel(name, false);
        registerCrucible(name, false);
    }

    void registerSieve(String name);

    void registerCrucible(String name, boolean isStone);

    void registerBarrel(String name, boolean isStone);

    void registerInfestedLeaves(String name, Identifier source);

    void registerTransformingSeed(String name, Lazy<Block> from, Lazy<BlockState> to);

    default void registerTransformingSeed(String name, Identifier from, Identifier to) {
        registerTransformingSeed(name, new Lazy<>(() -> Registry.BLOCK.get(from)), new Lazy<>(() -> Registry.BLOCK.get(to).getDefaultState()));
    }

    void registerTallPlantSeed(String name, Lazy<TallPlantBlock[]> plants);

    default void registerTallPlantSeed(String name, Identifier... plants) {
        registerTallPlantSeed(name, new Lazy<>(() -> Arrays.stream(plants)
                .map(Registry.BLOCK::get)
                .map(block -> (TallPlantBlock) block)
                .toArray(TallPlantBlock[]::new)));
    }

    void registerSeed(String name, Lazy<BlockState[]> plants);

    void registerSeed(String name, Lazy<BlockState[]> plants, Predicate<ItemUsageContext> placementCheck);

    default void registerSeed(String name, Identifier... plants) {
        registerSeed(name, new Lazy<>(() -> Arrays.stream(plants)
                .map(plant -> Registry.BLOCK.get(plant).getDefaultState())
                .toArray(BlockState[]::new)));
    }

}
