package wraith.fabricaeexnihilo.modules;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.KelpBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.farming.PlantableItem;
import wraith.fabricaeexnihilo.modules.farming.TallPlantableItem;
import wraith.fabricaeexnihilo.modules.farming.TransformingItem;
import wraith.fabricaeexnihilo.modules.infested.SilkWormItem;
import wraith.fabricaeexnihilo.modules.ores.OreItem;
import wraith.fabricaeexnihilo.modules.sieves.MeshItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModItems {

    public static final FabricItemSettings BASE_SETTINGS = new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP).maxCount(64);

    public static final Map<Identifier, Item> TREE_SEEDS = new HashMap<>();
    public static final Map<Identifier, Item> CROP_SEEDS = new HashMap<>();
    public static final Map<Identifier, Item> FLOWER_SEEDS = new HashMap<>();
    public static final Map<Identifier, Item> OTHER_SEEDS = new HashMap<>();

    public static final Map<Identifier, Item> RESOURCES = new HashMap<>();

    public static final Map<Identifier, OreItem> ORE_PIECES = new HashMap<>();
    public static final Map<Identifier, MeshItem> MESHES = new HashMap<>();

    public static final List<Identifier> DOLLS = new ArrayList<>();

    static {
        RESOURCES.put(id("andesite_pebble"), new Item(BASE_SETTINGS));
        RESOURCES.put(id("diorite_pebble"), new Item(BASE_SETTINGS));
        RESOURCES.put(id("granite_pebble"), new Item(BASE_SETTINGS));
        RESOURCES.put(id("stone_pebble"), new Item(BASE_SETTINGS));
        RESOURCES.put(id("blackstone_pebble"), new Item(BASE_SETTINGS));
        RESOURCES.put(id("basalt_pebble"), new Item(BASE_SETTINGS));
        RESOURCES.put(id("deepslate_pebble"), new Item(BASE_SETTINGS));

        RESOURCES.put(id("porcelain"), new Item(BASE_SETTINGS));
        RESOURCES.put(id("unfired_porcelain_crucible"), new Item(BASE_SETTINGS));
        RESOURCES.put(id("salt_bottle"), new Item(BASE_SETTINGS));

        DOLLS.add(id("doll"));
        DOLLS.add(id("doll_blaze"));
        DOLLS.add(id("doll_enderman"));
        DOLLS.add(id("doll_guardian"));
        DOLLS.add(id("doll_shulker"));
    }

    public static void registerItems() {
        // Setup Conditional Items.
        setup();
        // Register Seeds
        TREE_SEEDS.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
        CROP_SEEDS.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
        OTHER_SEEDS.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
        FLOWER_SEEDS.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));

        // Register Meshes
        MESHES.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));

        // Register Others
        RESOURCES.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
        DOLLS.forEach(doll -> Registry.register(Registry.ITEM, doll, new Item(BASE_SETTINGS)));

        // Register Ores
        ORE_PIECES.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
        ModFluids.registerBuckets();
    }

    public static void setup() {
        if (FabricaeExNihilo.CONFIG.modules.silkworms.enabled) {
            RESOURCES.put(id("raw_silkworm"), new SilkWormItem(new FabricItemSettings().maxCount(64).food(FoodComponents.COD).group(FabricaeExNihilo.ITEM_GROUP)));
            RESOURCES.put(id("cooked_silkworm"), new Item(new FabricItemSettings().maxCount(64).food(FoodComponents.COOKED_COD).group(FabricaeExNihilo.ITEM_GROUP)));
        }
        if (FabricaeExNihilo.CONFIG.modules.seeds.enabled) {
            if (FabricaeExNihilo.CONFIG.modules.seeds.carrot) {
                CROP_SEEDS.put(id("carrot_seeds"), new PlantableItem(Blocks.CARROTS, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.chorus) {
                CROP_SEEDS.put(id("chorus_seeds"), new PlantableItem(Blocks.CHORUS_FLOWER, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.grass) {
                OTHER_SEEDS.put(id("grass_seeds"), new TransformingItem(Blocks.DIRT, Blocks.GRASS_BLOCK, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.kelp) {
                CROP_SEEDS.put(id("kelp_seeds"), new PlantableItem(IntStream.range(0, 25).mapToObj(age -> Blocks.KELP.getDefaultState().with(KelpBlock.AGE, age)).toList(), BASE_SETTINGS) {
                    @Override
                    public boolean placementCheck(ItemUsageContext context) {
                        return context.getWorld().getFluidState(context.getBlockPos().offset(context.getSide())).getFluid() == Fluids.WATER;
                    }
                });
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.flowerSeeds) {
                FLOWER_SEEDS.put(id("sunflower_seeds"), new TallPlantableItem((TallPlantBlock) Blocks.SUNFLOWER, BASE_SETTINGS));
                FLOWER_SEEDS.put(id("lilac_seeds"), new TallPlantableItem((TallPlantBlock) Blocks.LILAC, BASE_SETTINGS));
                FLOWER_SEEDS.put(id("rose_bush_seeds"), new TallPlantableItem((TallPlantBlock) Blocks.ROSE_BUSH, BASE_SETTINGS));
                FLOWER_SEEDS.put(id("peony_seeds"), new TallPlantableItem((TallPlantBlock) Blocks.PEONY, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.mycelium) {
                OTHER_SEEDS.put(id("mycelium_seeds"), new TransformingItem(Blocks.DIRT, Blocks.MYCELIUM, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.potato) {
                CROP_SEEDS.put(id("potato_seeds"), new PlantableItem(Blocks.POTATOES, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.seaPickle) {
                OTHER_SEEDS.put(id("sea_pickle_seeds"), new PlantableItem(Blocks.SEA_PICKLE, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.sugarCane) {
                CROP_SEEDS.put(id("sugarcane_seeds"), new PlantableItem(Blocks.SUGAR_CANE, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.cactus) {
                CROP_SEEDS.put(id("cactus_seeds"), new PlantableItem(Blocks.CACTUS, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.treeSeeds) {
                TREE_SEEDS.put(id("oak_seeds"), new PlantableItem(Blocks.OAK_SAPLING, BASE_SETTINGS));
                TREE_SEEDS.put(id("birch_seeds"), new PlantableItem(Blocks.BIRCH_SAPLING, BASE_SETTINGS));
                TREE_SEEDS.put(id("spruce_seeds"), new PlantableItem(Blocks.SPRUCE_SAPLING, BASE_SETTINGS));
                TREE_SEEDS.put(id("jungle_seeds"), new PlantableItem(Blocks.JUNGLE_SAPLING, BASE_SETTINGS));
                TREE_SEEDS.put(id("acacia_seeds"), new PlantableItem(Blocks.ACACIA_SAPLING, BASE_SETTINGS));
                TREE_SEEDS.put(id("dark_oak_seeds"), new PlantableItem(Blocks.DARK_OAK_SAPLING, BASE_SETTINGS));
                // TODO: Check inside modules
                var rubberSaplings = FabricaeExNihilo.CONFIG.modules.seeds.rubberSeed.stream()
                        .map(rubberSeed -> Registry.BLOCK.get(new Identifier(rubberSeed)).getDefaultState())
                        // Remove unrecognized ones
                        .filter(blockstate -> !blockstate.isAir()).toList();
                if (!rubberSaplings.isEmpty()) {
                    TREE_SEEDS.put(id("rubber_seeds"), new PlantableItem(rubberSaplings, BASE_SETTINGS));
                }
            }
        }
    }
}
