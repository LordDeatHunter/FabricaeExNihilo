package wraith.fabricaeexnihilo.compatibility;

import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.FabricaeExNihiloConfig;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.util.Color;

public class DefaultApiModule implements FENApiModule {
    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("iron", Color.IRON);
        registries.registerOrePiece("copper", Color.COPPER);
        registries.registerOrePiece("gold", Color.GOLD);
        registries.registerWood("oak");
        registries.registerWood("birch");
        registries.registerWood("spruce");
        registries.registerWood("acacia");
        registries.registerWood("dark_oak");
        registries.registerWood("jungle");
        registries.registerWood("warped");
        registries.registerWood("crimson");
        
        registries.registerCrushedBlock("dust", true);
        registries.registerCrushedBlock("silt", true);
    
        registries.registerCrushedBlock("crushed_andesite", false);
        registries.registerCrushedBlock("crushed_diorite", false);
        registries.registerCrushedBlock("crushed_granite", false);
        registries.registerCrushedBlock("crushed_prismarine", false);
        registries.registerCrushedBlock("crushed_endstone", false);
        registries.registerCrushedBlock("crushed_netherrack", false);
    
        registries.registerCrucible("porcelain", true);

        registries.registerBarrel("stone", true);

        registries.registerMesh("string", Color.WHITE, 10);
        registries.registerMesh("flint", Color.GRAY, 12);
        registries.registerMesh("iron", new Color("777777"), 14);
        registries.registerMesh("diamond", Color.DARK_AQUA, 10);
        registries.registerMesh("netherite", new Color("3B393B"), 15);
        registries.registerMesh("copper", Color.COPPER, 13);
        registries.registerMesh("gold", Color.GOLDEN, 22);
        registries.registerMesh("emerald", Color.DARK_GREEN, 24);
        
        registries.registerInfestedLeaves("oak", new Identifier("minecraft:oak_leaves"));
        registries.registerInfestedLeaves("birch", new Identifier("minecraft:birch_leaves"));
        registries.registerInfestedLeaves("spruce", new Identifier("minecraft:spruce_leaves"));
        registries.registerInfestedLeaves("acacia", new Identifier("minecraft:acacia_leaves"));
        registries.registerInfestedLeaves("dark_oak", new Identifier("minecraft:dark_oak_leaves"));
        registries.registerInfestedLeaves("jungle", new Identifier("minecraft:jungle_leaves"));
    
        var seedConfig = FabricaeExNihilo.CONFIG.modules.seeds;
        if (seedConfig.enabled) {
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
            if (seedConfig.grass) {
                registries.registerTransformingSeed("grass", new Identifier("minecraft", "dirt"), new Identifier("minecraft", "grass_block"));
            }
            if (seedConfig.mycelium) {
                registries.registerTransformingSeed("mycelium", new Identifier("minecraft", "dirt"), new Identifier("minecraft", "mycelium"));
            }
            if (seedConfig.kelp) {
                registries.registerSeed("kelp", new Identifier("minecraft", "kelp"));
            }
            if (seedConfig.netherSpores) {
                registries.registerTransformingSeed("warped", new Identifier("minecraft", "netherrack"), new Identifier("minecraft", "warped_nylium"));
                registries.registerTransformingSeed("crimson", new Identifier("minecraft", "netherrack"), new Identifier("minecraft", "crimson_nylium"));
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
