package wraith.fabricaeexnihilo.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.api.FabricaeExNihiloApiModule;
import wraith.fabricaeexnihilo.api.ore.OreMaterial;
import wraith.fabricaeexnihilo.api.ore.OreShape;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlock;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlock;
import wraith.fabricaeexnihilo.modules.farming.PlantableItem;
import wraith.fabricaeexnihilo.modules.farming.TallPlantableItem;
import wraith.fabricaeexnihilo.modules.farming.TransformingItem;
import wraith.fabricaeexnihilo.modules.infested.InfestedLeavesBlock;
import wraith.fabricaeexnihilo.modules.ores.OreItem;
import wraith.fabricaeexnihilo.modules.sieves.MeshItem;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlock;

import java.util.function.Predicate;

public class EntrypointHelper {

    public static void callEntrypoints() {
        var entrypoints = FabricLoader.getInstance().getEntrypoints("fabricaeexnihilo:api", FabricaeExNihiloApiModule.class).stream().filter(FabricaeExNihiloApiModule::shouldLoad).toList();
        var registries = new FENRegistriesImpl();

        entrypoints.forEach(entrypoint -> entrypoint.register(registries));
    }

    private static Identifier id(String ore, @Nullable String prefix, @Nullable String suffix) {
        return FabricaeExNihilo.id((prefix == null ? "" : prefix) + ore + (suffix == null ? "" : suffix));
    }

    private static final class FENRegistriesImpl implements FENRegistries {

        @Override
        public void registerSandyCrushed(String name) {
            ModBlocks.CRUSHED.put(id(name, null, null), new FallingBlock(ModBlocks.CRUSHED_SANDY_SETTINGS));
        }

        @Override
        public void registerGravelyCrushed(String name) {
            ModBlocks.CRUSHED.put(id(name, null, null), new FallingBlock(ModBlocks.CRUSHED_GRAVELY_SETTINGS));
        }

        @Override
        public void registerOre(String name, Color color, OreShape oreShape, OreMaterial baseMaterial) {
            ModItems.ORE_PIECES.put(id(name, null, "_piece"), new OreItem(color, baseMaterial, oreShape));
        }

        @Override
        public void registerMesh(String name, Color color, int enchantability) {
            ModItems.MESHES.put(id(name, null, "_mesh"), new MeshItem(color, enchantability));
        }

        @Override
        public void registerSieve(String name) {
            ModBlocks.SIEVES.put(id(name, null, "_sieve"), new SieveBlock());
        }

        @Override
        public void registerCrucible(String name, boolean isStone) {
            ModBlocks.CRUCIBLES.put(id(name, null, "_crucible"), new CrucibleBlock(isStone ? ModBlocks.STONE_SETTINGS : ModBlocks.WOOD_SETTINGS));
        }

        @Override
        public void registerBarrel(String name, boolean isStone) {
            ModBlocks.BARRELS.put(id(name, null, "_barrel"), new BarrelBlock(isStone ? ModBlocks.STONE_SETTINGS : ModBlocks.WOOD_SETTINGS));
        }

        @Override
        public void registerInfestedLeaves(String name, Identifier source) {
            ModBlocks.INFESTED_LEAVES.put(id(name, "infested_", "_leaves"), new InfestedLeavesBlock(source, ModBlocks.INFESTED_LEAVES_SETTINGS));
        }

        @Override
        public void registerTransformingSeed(String name, Lazy<Block> from, Lazy<BlockState> to) {
            ModItems.SEEDS.put(id(name, null, "_seeds"), new TransformingItem(from, to, ModItems.BASE_SETTINGS));
        }

        @Override
        public void registerTallPlantSeed(String name, Lazy<TallPlantBlock[]> plants) {
            ModItems.SEEDS.put(id(name, null, "_seeds"), new TallPlantableItem(plants, ModItems.BASE_SETTINGS));
        }

        @Override
        public void registerSeed(String name, Lazy<BlockState[]> plants) {
            ModItems.SEEDS.put(id(name, null, "_seeds"), new PlantableItem(plants, ModItems.BASE_SETTINGS));
        }

        @Override
        public void registerSeed(String name, Lazy<BlockState[]> plants, Predicate<ItemUsageContext> placementCheck) {
            ModItems.SEEDS.put(id(name, null, "_seeds"), new PlantableItem(plants, ModItems.BASE_SETTINGS) {
                @Override
                public boolean placementCheck(ItemUsageContext context) {
                    return placementCheck.test(context);
                }
            });
        }
    }

}
