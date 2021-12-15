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
import wraith.fabricaeexnihilo.api.registry.FabricaeExNihiloRegistries;
import wraith.fabricaeexnihilo.modules.farming.PlantableItem;
import wraith.fabricaeexnihilo.modules.farming.TallPlantableItem;
import wraith.fabricaeexnihilo.modules.farming.TransformingItem;
import wraith.fabricaeexnihilo.modules.infested.SilkWormItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public final class ModItems {

    public static final FabricItemSettings BASE_SETTINGS = new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP).maxCount(64);

    public static final Map<Identifier, Item> TREE_SEEDS = new HashMap<>();
    public static final Map<Identifier, Item> CROP_SEEDS = new HashMap<>();
    public static final Map<Identifier, Item> FLOWER_SEEDS = new HashMap<>();
    public static final Map<Identifier, Item> OTHER_SEEDS = new HashMap<>();

    public static final Map<Identifier, Item> RESOURCES = new HashMap<>();

    public static final List<Identifier> DOLLS = new ArrayList<>();

    static {
        FLOWER_SEEDS.put(FabricaeExNihilo.id("seed_sunflower"), new TallPlantableItem((TallPlantBlock) Blocks.SUNFLOWER, BASE_SETTINGS));
        FLOWER_SEEDS.put(FabricaeExNihilo.id("seed_lilac"), new TallPlantableItem((TallPlantBlock) Blocks.LILAC, BASE_SETTINGS));
        FLOWER_SEEDS.put(FabricaeExNihilo.id("seed_rose_bush"), new TallPlantableItem((TallPlantBlock) Blocks.ROSE_BUSH, BASE_SETTINGS));
        FLOWER_SEEDS.put(FabricaeExNihilo.id("seed_peony"), new TallPlantableItem((TallPlantBlock) Blocks.PEONY, BASE_SETTINGS));

        RESOURCES.put(FabricaeExNihilo.id("pebble_andesite"), new Item(BASE_SETTINGS));
        RESOURCES.put(FabricaeExNihilo.id("pebble_diorite"), new Item(BASE_SETTINGS));
        RESOURCES.put(FabricaeExNihilo.id("pebble_granite"), new Item(BASE_SETTINGS));
        RESOURCES.put(FabricaeExNihilo.id("pebble_stone"), new Item(BASE_SETTINGS));
        RESOURCES.put(FabricaeExNihilo.id("porcelain"), new Item(BASE_SETTINGS));
        RESOURCES.put(FabricaeExNihilo.id("unfired_crucible"), new Item(BASE_SETTINGS));
        RESOURCES.put(FabricaeExNihilo.id("salt_bottle"), new Item(BASE_SETTINGS));

        DOLLS.add(FabricaeExNihilo.id("doll"));
        DOLLS.add(FabricaeExNihilo.id("doll_blaze"));
        DOLLS.add(FabricaeExNihilo.id("doll_enderman"));
        DOLLS.add(FabricaeExNihilo.id("doll_guardian"));
        DOLLS.add(FabricaeExNihilo.id("doll_shulker"));
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
        FabricaeExNihiloRegistries.MESH.registerItems();

        // Register Others
        RESOURCES.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
        DOLLS.forEach(doll -> Registry.register(Registry.ITEM, doll, new Item(BASE_SETTINGS)));

        // Register Ores
        FabricaeExNihiloRegistries.ORES.registerPieceItems();
        FabricaeExNihiloRegistries.ORES.registerChunkItems();

        ModFluids.registerBuckets();
    }

    public static void setup() {
        if (FabricaeExNihilo.CONFIG.modules.silkworms.enabled) {
            RESOURCES.put(FabricaeExNihilo.id("silkworm_raw"), new SilkWormItem(new FabricItemSettings().maxCount(64).food(FoodComponents.COD).group(FabricaeExNihilo.ITEM_GROUP)));
            RESOURCES.put(FabricaeExNihilo.id("silkworm_cooked"), new Item(new FabricItemSettings().maxCount(64).food(FoodComponents.COOKED_COD).group(FabricaeExNihilo.ITEM_GROUP)));
        }
        if (FabricaeExNihilo.CONFIG.modules.seeds.enabled) {
            if (FabricaeExNihilo.CONFIG.modules.seeds.carrot) {
                CROP_SEEDS.put(FabricaeExNihilo.id("seed_carrot"), new PlantableItem(Blocks.CARROTS, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.chorus) {
                CROP_SEEDS.put(FabricaeExNihilo.id("seed_chorus"), new PlantableItem(Blocks.CHORUS_FLOWER, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.grass) {
                OTHER_SEEDS.put(FabricaeExNihilo.id("seed_grass"), new TransformingItem(Blocks.DIRT, Blocks.GRASS_BLOCK, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.kelp) {
                CROP_SEEDS.put(FabricaeExNihilo.id("seed_kelp"), new PlantableItem(IntStream.range(0, 25).mapToObj(age -> Blocks.KELP.getDefaultState().with(KelpBlock.AGE, age)).toList(), BASE_SETTINGS) {
                    @Override
                    public boolean placementCheck(ItemUsageContext context) {
                        return context.getWorld().getFluidState(context.getBlockPos().offset(context.getSide())).getFluid() == Fluids.WATER;
                    }
                });
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.mycelium) {
                OTHER_SEEDS.put(FabricaeExNihilo.id("seed_mycelium"), new TransformingItem(Blocks.DIRT, Blocks.MYCELIUM, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.potato) {
                CROP_SEEDS.put(FabricaeExNihilo.id("seed_potato"), new PlantableItem(Blocks.POTATOES, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.seaPickle) {
                OTHER_SEEDS.put(FabricaeExNihilo.id("seed_sea_pickle"), new PlantableItem(Blocks.SEA_PICKLE, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.sugarCane) {
                CROP_SEEDS.put(FabricaeExNihilo.id("seed_sugarcane"), new PlantableItem(Blocks.SUGAR_CANE, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.cactus) {
                CROP_SEEDS.put(FabricaeExNihilo.id("seed_cactus"), new PlantableItem(Blocks.CACTUS, BASE_SETTINGS));
            }
            if (FabricaeExNihilo.CONFIG.modules.seeds.treeSeeds) {
                TREE_SEEDS.put(FabricaeExNihilo.id("seed_oak"), new PlantableItem(Blocks.OAK_SAPLING, BASE_SETTINGS));
                TREE_SEEDS.put(FabricaeExNihilo.id("seed_birch"), new PlantableItem(Blocks.BIRCH_SAPLING, BASE_SETTINGS));
                TREE_SEEDS.put(FabricaeExNihilo.id("seed_spruce"), new PlantableItem(Blocks.SPRUCE_SAPLING, BASE_SETTINGS));
                TREE_SEEDS.put(FabricaeExNihilo.id("seed_jungle"), new PlantableItem(Blocks.JUNGLE_SAPLING, BASE_SETTINGS));
                TREE_SEEDS.put(FabricaeExNihilo.id("seed_acacia"), new PlantableItem(Blocks.ACACIA_SAPLING, BASE_SETTINGS));
                TREE_SEEDS.put(FabricaeExNihilo.id("seed_dark_oak"), new PlantableItem(Blocks.DARK_OAK_SAPLING, BASE_SETTINGS));
                var rubberSaplings = FabricaeExNihilo.CONFIG.modules.seeds.rubberSeed.stream()
                        .map(rubberSeed -> Registry.BLOCK.get(new Identifier(rubberSeed)).getDefaultState())
                        // Remove unrecognized ones
                        .filter(blockstate -> !blockstate.isAir()).toList();
                if (!rubberSaplings.isEmpty()) {
                    TREE_SEEDS.put(FabricaeExNihilo.id("seed_rubber"), new PlantableItem(rubberSaplings, BASE_SETTINGS));
                }
            }
        }
    }
}
