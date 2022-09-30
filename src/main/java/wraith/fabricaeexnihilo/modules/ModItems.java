package wraith.fabricaeexnihilo.modules;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.infested.SilkWormItem;
import wraith.fabricaeexnihilo.modules.sieves.MeshItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModItems {

    public static final FabricItemSettings BASE_SETTINGS = new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP);
    public static final List<Identifier> DOLLS = new ArrayList<>();
    public static final Map<Identifier, MeshItem> MESHES = new LinkedHashMap<>();
    public static final Map<Identifier, Item> ORE_PIECES = new LinkedHashMap<>();
    public static final Map<Identifier, Item> PEBBLES = new LinkedHashMap<>();
    public static final Map<Identifier, Item> SEEDS = new LinkedHashMap<>();

    public static final Item PORCELAIN;
    public static final Item UNFIRED_PORCELAIN_CRUCIBLE;
    public static final Item SALT_BOTTLE;
    public static final Item RAW_SILKWORM;
    public static final Item COOKED_SILKWORM;

    static {
        // TODO: replace RESOURCES with PEBBLES and single block fields
        PEBBLES.put(id("andesite_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("basalt_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("blackstone_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("calcite_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("dripstone_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("deepslate_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("diorite_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("granite_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("stone_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("tuff_pebble"), new Item(BASE_SETTINGS));

        PORCELAIN = new Item(BASE_SETTINGS);
        UNFIRED_PORCELAIN_CRUCIBLE = new Item(BASE_SETTINGS);
        SALT_BOTTLE = new Item(BASE_SETTINGS);
        RAW_SILKWORM = new SilkWormItem(new FabricItemSettings().maxCount(64).food(FoodComponents.COD).group(FabricaeExNihilo.ITEM_GROUP));
        COOKED_SILKWORM = new Item(new FabricItemSettings().maxCount(64).food(FoodComponents.COOKED_COD).group(FabricaeExNihilo.ITEM_GROUP));

        DOLLS.add(id("doll"));
        DOLLS.add(id("doll_blaze"));
        DOLLS.add(id("doll_enderman"));
        DOLLS.add(id("doll_guardian"));
        DOLLS.add(id("doll_shulker"));
    }

    public static void registerItems() {
        // Register stuff
        Registry.register(Registry.ITEM, id("porcelain"), PORCELAIN);
        Registry.register(Registry.ITEM, id("unfired_porcelain_crucible"), UNFIRED_PORCELAIN_CRUCIBLE);
        Registry.register(Registry.ITEM, id("salt_bottle"), SALT_BOTTLE);
        Registry.register(Registry.ITEM, id("raw_silkworm"), RAW_SILKWORM);
        Registry.register(Registry.ITEM, id("cooked_silkworm"), COOKED_SILKWORM);

        SEEDS.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
        MESHES.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
        PEBBLES.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
        DOLLS.forEach(doll -> Registry.register(Registry.ITEM, doll, new Item(BASE_SETTINGS)));
        ORE_PIECES.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
        Registry.register(Registry.ITEM, id("end_cake"), new BlockItem(ModBlocks.END_CAKE, new FabricItemSettings().maxCount(1).group(FabricaeExNihilo.ITEM_GROUP)));
        ModFluids.registerBuckets();
    }

}
