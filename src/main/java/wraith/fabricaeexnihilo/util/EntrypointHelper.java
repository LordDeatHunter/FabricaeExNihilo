package wraith.fabricaeexnihilo.util;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlock;
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
        public void registerBarrel(String name, boolean isFireproof, AbstractBlock.Settings settings) {
            ModBlocks.BARRELS.put(
                id(name, null, "_barrel"),
                new BarrelBlock(settings, isFireproof)
            );
        }
    
        @Override
        public void registerCrucible(String name, boolean isFireproof, AbstractBlock.Settings settings) {
            ModBlocks.CRUCIBLES.put(
                    id(name, null, "_crucible"),
                    new CrucibleBlock(settings, isFireproof)
            );
        }
    
        @Override
        public void registerCrushedBlock(String name, AbstractBlock.Settings settings) {
            ModBlocks.CRUSHED.put(
                    id(name, null, null),
                    new FallingBlock(settings)
            );
        }
    
        @Override
        public void registerSieve(String name, boolean isFireproof, AbstractBlock.Settings settings) {
            ModBlocks.SIEVES.put(id(name, null, "_sieve"), new SieveBlock(settings));
        }
    
        @Override
        public void registerStrainer(String name, boolean isFireproof, AbstractBlock.Settings settings) {
            ModBlocks.STRAINERS.put(id(name, null, "_strainer"), new StrainerBlock(settings));
        }
    
        @Override
        public void registerInfestedLeaves(String name, Identifier source, AbstractBlock.Settings settings) {
            //TODO: Make these stack somehow: multiple sources for one result.
            ModBlocks.INFESTED_LEAVES.put(
                    id(name, "infested_", "_leaves"),
                    new InfestedLeavesBlock(source, settings)
            );
        }

        @Override
        public void registerMesh(String name, Color color, int enchantability, Item.Settings settings) {
            ModItems.MESHES.put(
                id(name, null, "_mesh"),
                new MeshItem(color, enchantability, settings)
            );
        }

        @Override
        public void registerOrePiece(String name, Item.Settings settings) {
            ModItems.ORE_PIECES.put(id(name, "raw_", "_piece"), new Item(ModItems.BASE_SETTINGS));
        }

        @Override
        public void registerSeed(String name, Lazy<Block[]> plants) {
            ModItems.SEEDS.put(id(name, null, "_seeds"), new PlantableItem(plants, ModItems.BASE_SETTINGS));
        }

        @Override
        public void registerTallPlantSeed(String name, Lazy<TallPlantBlock[]> plants) {
            ModItems.SEEDS.put(id(name, null, "_seeds"), new TallPlantableItem(plants, ModItems.BASE_SETTINGS));
        }

        @Override
        public void registerTransformingSeed(String name, Lazy<Block> from, Lazy<Block> to) {
            ModItems.SEEDS.put(id(name, null, "_seeds"), new TransformingItem(from, to, ModItems.BASE_SETTINGS));
        }
    
        @Override
        public FabricItemSettings defaultItemSettings() {
            return new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP);
        }
    
        @Override
        public FabricBlockSettings woodenBlockSettings() {
            return FabricBlockSettings.copyOf(FabricBlockSettings.of(Material.WOOD).strength(2.0f).sounds(BlockSoundGroup.WOOD));
        }
    
        @Override
        public FabricBlockSettings stoneBlockSettings() {
            return FabricBlockSettings.copyOf(FabricBlockSettings.of(Material.STONE).strength(2.0f, 6.0f).sounds(BlockSoundGroup.STONE).requiresTool());
        }
    
        @Override
        public FabricBlockSettings infestedLeavesBlockSettings() {
            return FabricBlockSettings.copyOf(ModBlocks.INFESTED_LEAVES_SETTINGS);
        }
    
        @Override
        public FabricBlockSettings sandyBlockSettings() {
            return FabricBlockSettings.copyOf(FabricBlockSettings.of(Material.AGGREGATE).strength(0.4f).sounds(BlockSoundGroup.SAND));
        }
    
        @Override
        public FabricBlockSettings gravelyBlockSettings() {
            return FabricBlockSettings.copyOf(FabricBlockSettings.of(Material.AGGREGATE).strength(0.6f).sounds(BlockSoundGroup.GRAVEL));
        }
    }
}
