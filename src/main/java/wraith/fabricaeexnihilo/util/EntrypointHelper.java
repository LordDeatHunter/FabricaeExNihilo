package wraith.fabricaeexnihilo.util;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlock;
import wraith.fabricaeexnihilo.modules.base.ColoredItem;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlock;
import wraith.fabricaeexnihilo.modules.farming.PlantableItem;
import wraith.fabricaeexnihilo.modules.farming.TallPlantableItem;
import wraith.fabricaeexnihilo.modules.farming.TransformingItem;
import wraith.fabricaeexnihilo.modules.infested.InfestedLeavesBlock;
import wraith.fabricaeexnihilo.modules.sieves.MeshItem;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlock;
import wraith.fabricaeexnihilo.modules.strainer.StrainerBlock;

public class EntrypointHelper {

    public static void callEntrypoints() {
        var entrypoints = FabricLoader.getInstance().getEntrypoints("fabricaeexnihilo:api", FENApiModule.class).stream().filter(FENApiModule::shouldLoad).toList();
        var registries = new FENRegistriesImpl();

        entrypoints.forEach(entrypoint -> entrypoint.onInit(registries));
    }

    private static Identifier id(String ore, @Nullable String prefix, @Nullable String suffix) {
        return FabricaeExNihilo.id((prefix == null ? "" : prefix) + ore + (suffix == null ? "" : suffix));
    }

    private static final class FENRegistriesImpl implements FENRegistries {

        @Override
        public void registerBarrel(String name, boolean isStone, boolean isFireproof) {
            ModBlocks.BARRELS.put(
                id(name, null, "_barrel"),
                new BarrelBlock(isStone ?
                    FabricBlockSettings.copyOf(ModBlocks.STONE_SETTINGS) :
                    FabricBlockSettings.copyOf(ModBlocks.WOOD_SETTINGS), isFireproof
                )
            );
        }

        @Override
        public void registerCrucible(String name, boolean isStone, boolean isFireproof) {
            ModBlocks.CRUCIBLES.put(
                id(name, null, "_crucible"),
                new CrucibleBlock(isStone ?
                    FabricBlockSettings.copyOf(ModBlocks.STONE_SETTINGS) :
                    FabricBlockSettings.copyOf(ModBlocks.WOOD_SETTINGS), isFireproof
                )
            );
        }

        @Override
        public void registerCrushedBlock(String name, boolean isSandy) {
            ModBlocks.CRUSHED.put(
                id(name, null, null),
                new FallingBlock(isSandy ?
                    FabricBlockSettings.copyOf(ModBlocks.CRUSHED_SANDY_SETTINGS) :
                    FabricBlockSettings.copyOf(ModBlocks.CRUSHED_GRAVELY_SETTINGS)
                )
            );
        }

        @Override
        public void registerInfestedLeaves(String name, Identifier source) {
            //TODO: Make these stack somehow: multiple sources for one result.
            ModBlocks.INFESTED_LEAVES.put(
                id(name, "infested_", "_leaves"),
                new InfestedLeavesBlock(source, FabricBlockSettings.copyOf(ModBlocks.INFESTED_LEAVES_SETTINGS))
            );
        }

        @Override
        public void registerMesh(String name, Color color, int enchantability) {
            ModItems.MESHES.put(
                id(name, null, "_mesh"),
                new MeshItem(color, enchantability)
            );
        }

        @Override
        public void registerOrePiece(String name, Color color) {
            ModItems.ORE_PIECES.put(id(name, null, "_piece"), new ColoredItem(color, ModItems.BASE_SETTINGS));
        }

        @Override
        public void registerSeed(String name, Lazy<Block[]> plants) {
            ModItems.SEEDS.put(id(name, null, "_seeds"), new PlantableItem(plants, ModItems.BASE_SETTINGS));
        }

        @Override
        public void registerSieve(String name) {
            ModBlocks.SIEVES.put(id(name, null, "_sieve"), new SieveBlock());
        }

        @Override
        public void registerStrainer(String name) {
            ModBlocks.STRAINERS.put(id(name, null, "_strainer"), new StrainerBlock());
        }

        @Override
        public void registerTallPlantSeed(String name, Lazy<TallPlantBlock[]> plants) {
            ModItems.SEEDS.put(id(name, null, "_seeds"), new TallPlantableItem(plants, ModItems.BASE_SETTINGS));
        }

        @Override
        public void registerTransformingSeed(String name, Lazy<Block> from, Lazy<Block> to) {
            ModItems.SEEDS.put(id(name, null, "_seeds"), new TransformingItem(from, to, ModItems.BASE_SETTINGS));
        }

    }

}
