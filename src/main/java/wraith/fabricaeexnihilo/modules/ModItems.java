package wraith.fabricaeexnihilo.modules;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.base.ColoredItem;
import wraith.fabricaeexnihilo.modules.infested.SilkWormItem;
import wraith.fabricaeexnihilo.modules.sieves.MeshItem;

import java.util.*;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModItems {

    public static final FabricItemSettings BASE_SETTINGS = new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP).maxCount(64);
    
    public static final Map<Identifier, Item> SEEDS = new LinkedHashMap<>();

    public static final Map<Identifier, Item> RESOURCES = new LinkedHashMap<>();

    public static final Map<Identifier, ColoredItem> ORE_PIECES = new LinkedHashMap<>();
    public static final Map<Identifier, MeshItem> MESHES = new LinkedHashMap<>();

    public static final List<Identifier> DOLLS = new ArrayList<>();

    static {
        RESOURCES.put(id("andesite_pebble"), new Item(BASE_SETTINGS));
        RESOURCES.put(id("basalt_pebble"), new Item(BASE_SETTINGS));
        RESOURCES.put(id("blackstone_pebble"), new Item(BASE_SETTINGS));
        RESOURCES.put(id("deepslate_pebble"), new Item(BASE_SETTINGS));
        RESOURCES.put(id("diorite_pebble"), new Item(BASE_SETTINGS));
        RESOURCES.put(id("granite_pebble"), new Item(BASE_SETTINGS));
        RESOURCES.put(id("stone_pebble"), new Item(BASE_SETTINGS));

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
        SEEDS.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));

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
    }
}
