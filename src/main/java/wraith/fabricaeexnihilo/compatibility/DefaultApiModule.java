package wraith.fabricaeexnihilo.compatibility;

import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.util.Color;

public class DefaultApiModule implements FENApiModule {

    @Override
    public void onInit(FENRegistries registries) {
        registries.registerOrePiece("iron", registries.defaultItemSettings());
        registries.registerOrePiece("copper", registries.defaultItemSettings());
        registries.registerOrePiece("gold", registries.defaultItemSettings());

        registries.registerWood("oak", false, registries.woodenBlockSettings());
        registries.registerWood("birch", false, registries.woodenBlockSettings());
        registries.registerWood("spruce", false, registries.woodenBlockSettings());
        registries.registerWood("acacia", false, registries.woodenBlockSettings());
        registries.registerWood("dark_oak", false, registries.woodenBlockSettings());
        registries.registerWood("jungle", false, registries.woodenBlockSettings());
        registries.registerWood("warped", true, registries.woodenBlockSettings());
        registries.registerWood("crimson", true, registries.woodenBlockSettings());

        registries.registerCrushedBlock("dust", registries.sandyBlockSettings());
        registries.registerCrushedBlock("silt", registries.sandyBlockSettings());
        registries.registerCrushedBlock("crushed_calcite", registries.sandyBlockSettings());

        registries.registerCrushedBlock("crushed_andesite", registries.gravelyBlockSettings());
        registries.registerCrushedBlock("crushed_diorite", registries.gravelyBlockSettings());
        registries.registerCrushedBlock("crushed_granite", registries.gravelyBlockSettings());
        registries.registerCrushedBlock("crushed_prismarine", registries.gravelyBlockSettings());
        registries.registerCrushedBlock("crushed_endstone", registries.gravelyBlockSettings());
        registries.registerCrushedBlock("crushed_netherrack", registries.gravelyBlockSettings());

        registries.registerCrucible("porcelain", true, registries.stoneBlockSettings());
        registries.registerBarrel("stone", true, registries.stoneBlockSettings());

        registries.registerInfestedLeaves("oak", new Identifier("minecraft:oak_leaves"), registries.infestedLeavesBlockSettings());
        registries.registerInfestedLeaves("birch", new Identifier("minecraft:birch_leaves"), registries.infestedLeavesBlockSettings());
        registries.registerInfestedLeaves("spruce", new Identifier("minecraft:spruce_leaves"), registries.infestedLeavesBlockSettings());
        registries.registerInfestedLeaves("acacia", new Identifier("minecraft:acacia_leaves"), registries.infestedLeavesBlockSettings());
        registries.registerInfestedLeaves("dark_oak", new Identifier("minecraft:dark_oak_leaves"), registries.infestedLeavesBlockSettings());
        registries.registerInfestedLeaves("jungle", new Identifier("minecraft:jungle_leaves"), registries.infestedLeavesBlockSettings());

        registries.registerMesh("string", Color.WHITE, 10, registries.defaultItemSettings());
        registries.registerMesh("flint", Color.GRAY, 12, registries.defaultItemSettings());
        registries.registerMesh("iron", new Color("777777"), 14, registries.defaultItemSettings());
        registries.registerMesh("diamond", Color.DARK_AQUA, 10, registries.defaultItemSettings());
        registries.registerMesh("netherite", new Color("3B393B"), 15, registries.defaultItemSettings());
        registries.registerMesh("copper", Color.COPPER, 13, registries.defaultItemSettings());
        registries.registerMesh("gold", Color.GOLDEN, 22, registries.defaultItemSettings());
        registries.registerMesh("emerald", Color.DARK_GREEN, 24, registries.defaultItemSettings());

        var seedConfig = FabricaeExNihilo.CONFIG.modules.seeds;
        if (seedConfig.enabled) {
            if (seedConfig.mycelium) {
                registries.registerTransformingSeed("mycelium", new Identifier("minecraft:dirt"), new Identifier("minecraft:mycelium"));
            }
            if (seedConfig.netherSpores) {
                registries.registerTransformingSeed("warped", new Identifier("minecraft:netherrack"), new Identifier("minecraft:warped_nylium"));
                registries.registerTransformingSeed("crimson", new Identifier("minecraft:netherrack"), new Identifier("minecraft:crimson_nylium"));
            }
            if (seedConfig.grass) {
                registries.registerTransformingSeed("grass", new Identifier("minecraft:dirt"), new Identifier("minecraft:grass_block"));
            }
            //TODO: Replace seeds bellow with vanilla variants
            if (seedConfig.chorus) {
                registries.registerSeed("chorus", new Identifier("minecraft:chorus_flower"));
            }
            if (seedConfig.seaPickle) {
                registries.registerSeed("sea_pickle", new Identifier("minecraft:sea_pickle"));
            }
            if (seedConfig.sugarCane) {
                registries.registerSeed("sugarcane", new Identifier("minecraft:sugar_cane"));
            }
            if (seedConfig.cactus) {
                registries.registerSeed("cactus", new Identifier("minecraft:cactus"));
            }
            if (seedConfig.kelp) {
                registries.registerSeed("kelp", new Identifier("minecraft:kelp"));
            }
            if (seedConfig.flowerSeeds) {
                registries.registerTallPlantSeed("sunflower", new Identifier("minecraft:sunflower"));
                registries.registerTallPlantSeed("lilac", new Identifier("minecraft:lilac"));
                registries.registerTallPlantSeed("rose_bush", new Identifier("minecraft:rose_bush"));
                registries.registerTallPlantSeed("peony", new Identifier("minecraft:peony"));
            }
        }
    }
}
