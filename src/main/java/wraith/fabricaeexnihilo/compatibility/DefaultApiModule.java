package wraith.fabricaeexnihilo.compatibility;

import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.util.Color;

import static wraith.fabricaeexnihilo.modules.ModBlocks.*;
import static wraith.fabricaeexnihilo.modules.ModItems.itemSettings;

public class DefaultApiModule implements FENApiModule {

    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("iron", itemSettings());
        registries.registerOrePiece("copper", itemSettings());
        registries.registerOrePiece("gold", itemSettings());
        
        registries.registerWood("oak", false, woodSettings());
        registries.registerWood("birch", false, woodSettings());
        registries.registerWood("spruce", false, woodSettings());
        registries.registerWood("acacia", false, woodSettings());
        registries.registerWood("dark_oak", false, woodSettings());
        registries.registerWood("jungle", false, woodSettings());
        registries.registerWood("warped", false, woodSettings());
        registries.registerWood("crimson", false, woodSettings());
    
        registries.registerCrushedBlock("dust", sandySettings());
        registries.registerCrushedBlock("silt", sandySettings());

        registries.registerCrushedBlock("crushed_andesite", gravelySettings());
        registries.registerCrushedBlock("crushed_diorite", gravelySettings());
        registries.registerCrushedBlock("crushed_granite", gravelySettings());
        registries.registerCrushedBlock("crushed_prismarine", gravelySettings());
        registries.registerCrushedBlock("crushed_endstone", gravelySettings());
        registries.registerCrushedBlock("crushed_netherrack", gravelySettings());
    
        registries.registerCrucible("porcelain", true, stoneSettings());
        registries.registerBarrel("stone", true, stoneSettings());
        
        registries.registerInfestedLeaves("oak", new Identifier("minecraft:oak_leaves"), infestedLeavesSettings());
        registries.registerInfestedLeaves("birch", new Identifier("minecraft:birch_leaves"), infestedLeavesSettings());
        registries.registerInfestedLeaves("spruce", new Identifier("minecraft:spruce_leaves"), infestedLeavesSettings());
        registries.registerInfestedLeaves("acacia", new Identifier("minecraft:acacia_leaves"), infestedLeavesSettings());
        registries.registerInfestedLeaves("dark_oak", new Identifier("minecraft:dark_oak_leaves"), infestedLeavesSettings());
        registries.registerInfestedLeaves("jungle", new Identifier("minecraft:jungle_leaves"), infestedLeavesSettings());
    
        registries.registerMesh("string", Color.WHITE, 10, itemSettings());
        registries.registerMesh("flint", Color.GRAY, 12, itemSettings());
        registries.registerMesh("iron", new Color("777777"), 14, itemSettings());
        registries.registerMesh("diamond", Color.DARK_AQUA, 10, itemSettings());
        registries.registerMesh("netherite", new Color("3B393B"), 15, itemSettings());
        registries.registerMesh("copper", Color.COPPER, 13, itemSettings());
        registries.registerMesh("gold", Color.GOLDEN, 22, itemSettings());
        registries.registerMesh("emerald", Color.DARK_GREEN, 24, itemSettings());
        
        var seedConfig = FabricaeExNihilo.CONFIG.modules.seeds;
        if (seedConfig.enabled) {
            if (seedConfig.mycelium) {
                registries.registerTransformingSeed("mycelium", new Identifier("minecraft", "dirt"), new Identifier("minecraft", "mycelium"));
            }
            if (seedConfig.netherSpores) {
                registries.registerTransformingSeed("warped", new Identifier("minecraft", "netherrack"), new Identifier("minecraft", "warped_nylium"));
                registries.registerTransformingSeed("crimson", new Identifier("minecraft", "netherrack"), new Identifier("minecraft", "crimson_nylium"));
            }
            if (seedConfig.grass) {
                registries.registerTransformingSeed("grass", new Identifier("minecraft", "dirt"), new Identifier("minecraft", "grass_block"));
            }
            //TODO: Replace seeds bellow with vanilla variants
            if (seedConfig.carrot) {
                registries.registerSeed("carrot", new Identifier("minecraft", "carrots"));
            }
            if (seedConfig.potato) {
                registries.registerSeed("potato", new Identifier("minecraft", "potatoes"));
            }
            if (seedConfig.chorus) {
                registries.registerSeed("chorus", new Identifier("minecraft", "chorus_flower"));
            }
            if (seedConfig.seaPickle) {
                registries.registerSeed("sea_pickle", new Identifier("minecraft", "sea_pickle"));
            }
            if (seedConfig.sugarCane) {
                registries.registerSeed("sugarcane", new Identifier("minecraft", "sugar_cane"));
            }
            if (seedConfig.cactus) {
                registries.registerSeed("cactus", new Identifier("minecraft", "cactus"));
            }
            if (seedConfig.kelp) {
                registries.registerSeed("kelp", new Identifier("minecraft", "kelp"));
            }
            if (seedConfig.flowerSeeds) {
                registries.registerTallPlantSeed("sunflower", new Identifier("minecraft", "sunflower"));
                registries.registerTallPlantSeed("lilac", new Identifier("minecraft", "lilac"));
                registries.registerTallPlantSeed("rose_bush", new Identifier("minecraft", "rose_bush"));
                registries.registerTallPlantSeed("peony", new Identifier("minecraft", "peony"));
            }
            if (seedConfig.treeSeeds) {
                registries.registerSeed("oak", new Identifier("minecraft", "oak_sapling"));
                registries.registerSeed("birch", new Identifier("minecraft", "birch_sapling"));
                registries.registerSeed("spruce", new Identifier("minecraft", "spruce_sapling"));
                registries.registerSeed("jungle", new Identifier("minecraft", "jungle_sapling"));
                registries.registerSeed("acacia", new Identifier("minecraft", "acacia_sapling"));
                registries.registerSeed("dark_oak", new Identifier("minecraft", "dark_oak_sapling"));
            }
        }
    }
}
