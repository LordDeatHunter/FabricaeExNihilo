package wraith.fabricaeexnihilo.compatibility;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.KelpBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.api.FabricaeExNihiloApiModule;
import wraith.fabricaeexnihilo.api.ore.OreMaterial;
import wraith.fabricaeexnihilo.api.ore.OreShape;
import wraith.fabricaeexnihilo.util.Lazy;
import wraith.fabricaeexnihilo.util.Color;

import java.util.stream.IntStream;

/* TODO: More api modules:
    Modern Industrialisation: https://github.com/AztechMC/Modern-Industrialization/blob/master/src/main/java/aztech/modern_industrialization/materials/MIMaterials.java
    Industrial Revolution
    + any other mods that add ores or trees...
         */
public class DefaultApiModule implements FabricaeExNihiloApiModule {
    @Override
    public void register(FENRegistries registries) {
        registries.registerOre("iron", Color.IRON, OreShape.NORMAL, OreMaterial.GRANITE);
        registries.registerOre("gold", Color.GOLD, OreShape.FINE, OreMaterial.STONE);
        
        registries.registerWood("oak");
        registries.registerWood("birch");
        registries.registerWood("spruce");
        registries.registerWood("acacia");
        registries.registerWood("dark_oak");
        registries.registerWood("jungle");

        registries.registerSandyCrushed("dust");
        registries.registerSandyCrushed("sand");

        registries.registerGravelyCrushed("crushed_andesite");
        registries.registerGravelyCrushed("crushed_diorite");
        registries.registerGravelyCrushed("crushed_granite");
        registries.registerGravelyCrushed("crushed_prismarine");
        registries.registerGravelyCrushed("crushed_endstone");
        registries.registerGravelyCrushed("crushed_netherrack");

        registries.registerCrucible("porcelain_crucible", true);

        registries.registerBarrel("stone_barrel", true);

        registries.registerMesh("string", Color.WHITE, 10);
        registries.registerMesh("flint", Color.GRAY, 12);
        registries.registerMesh("iron", new Color("777777"), 14);
        registries.registerMesh("gold", Color.GOLDEN, 22);
        registries.registerMesh("copper", Color.COPPER, 13);
        registries.registerMesh("emerald", Color.DARK_GREEN, 24);
        registries.registerMesh("diamond", Color.DARK_AQUA, 10);
        registries.registerMesh("netherite", new Color("3B393B"), 15);
        
        registries.registerInfestedLeaves("oak", new Identifier("minecraft:oak_leaves"));
        registries.registerInfestedLeaves("birch", new Identifier("minecraft:birch_leaves"));
        registries.registerInfestedLeaves("spruce", new Identifier("minecraft:spruce_leaves"));
        registries.registerInfestedLeaves("acacia", new Identifier("minecraft:acacia_leaves"));
        registries.registerInfestedLeaves("dark_oak", new Identifier("minecraft:dark_oak_leaves"));
        registries.registerInfestedLeaves("jungle", new Identifier("minecraft:jungle_leaves"));
    
        if (FabricaeExNihilo.CONFIG.modules.seeds.enabled) {
            if (FabricaeExNihilo.CONFIG.modules.seeds.carrot) {
                registries.registerSeed("carrot", new Identifier("minecraft", "carrot"));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.potato) {
                registries.registerSeed("potato", new Identifier("minecraft", "potato"));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.chorus) {
                registries.registerSeed("chorus", new Identifier("minecraft", "chorus_flower"));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.seaPickle) {
                registries.registerSeed("sea_pickle", new Identifier("minecraft", "sea_pickle"));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.sugarCane) {
                registries.registerSeed("sugarcane", new Identifier("minecraft", "sugarcane"));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.cactus) {
                registries.registerSeed("cactus", new Identifier("minecraft", "cactus"));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.grass) {
                registries.registerTransformingSeed("grass", new Identifier("minecraft", "dirt"), new Identifier("minecraft", "grass_block"));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.mycelium) {
                registries.registerTransformingSeed("mycelium", new Identifier("minecraft", "dirt"), new Identifier("minecraft", "mycelium"));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.kelp) {
                registries.registerSeed("kelp", new Lazy<>(() -> IntStream.range(0, 25)
                                .mapToObj(age -> Blocks.KELP.getDefaultState().with(KelpBlock.AGE, age))
                                .toArray(BlockState[]::new)),
                        context -> context.getWorld().getFluidState(context.getBlockPos().offset(context.getSide())).getFluid() == Fluids.WATER);
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.flowerSeeds) {
                registries.registerTallPlantSeed("sunflower", new Identifier("minecraft", "sunflower"));
                registries.registerTallPlantSeed("lilac", new Identifier("minecraft", "lilac"));
                registries.registerTallPlantSeed("rose_bush", new Identifier("minecraft", "rose_bush"));
                registries.registerTallPlantSeed("peony", new Identifier("minecraft", "peony"));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.treeSeeds) {
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
