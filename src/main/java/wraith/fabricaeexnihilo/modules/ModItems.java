package wraith.fabricaeexnihilo.modules;

import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.infested.SilkWormItem;
import wraith.fabricaeexnihilo.modules.sieves.MeshItem;

import java.util.LinkedHashMap;
import java.util.Map;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModItems {

    public static Item.Settings createSettings(Identifier id) {
        return new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id));
    }

    public static final Item COOKED_SILKWORM;
    public static final Map<Identifier, Item> DOLLS = new LinkedHashMap<>();
    public static final Map<Identifier, MeshItem> MESHES = new LinkedHashMap<>();
    public static final Map<Identifier, Item> ORE_PIECES = new LinkedHashMap<>();
    public static final Map<Identifier, Item> PEBBLES = new LinkedHashMap<>();
    public static final Item PORCELAIN;
    public static final Item RAW_SILKWORM;
    public static final Item SALT_BOTTLE;
    public static final Map<Identifier, Item> SEEDS = new LinkedHashMap<>();
    public static final Item UNFIRED_PORCELAIN_CRUCIBLE;

    static {
        // TODO: replace RESOURCES with PEBBLES and single block fields
        putPebble("andesite_pebble");
        putPebble("basalt_pebble");
        putPebble("blackstone_pebble");
        putPebble("calcite_pebble");
        putPebble("dripstone_pebble");
        putPebble("deepslate_pebble");
        putPebble("diorite_pebble");
        putPebble("granite_pebble");
        putPebble("stone_pebble");
        putPebble("tuff_pebble");

        PORCELAIN = new Item(createSettings(id("porcelain")));
        UNFIRED_PORCELAIN_CRUCIBLE = new Item(createSettings(id("unfired_porcelain_crucible")));
        SALT_BOTTLE = new Item(createSettings(id("salt_bottle")));
        RAW_SILKWORM = new SilkWormItem(createSettings(id("raw_silkworm")).maxCount(64).food(FoodComponents.COD));
        COOKED_SILKWORM = new Item(createSettings(id("cooked_silkworm")).maxCount(64).food(FoodComponents.COOKED_COD));

        putDoll("doll");
        putDoll("doll_blaze");
        putDoll("doll_enderman");
        putDoll("doll_guardian");
        putDoll("doll_shulker");
    }

    private static void putPebble(String name) {
        var identifier = id(name);
        PEBBLES.put(identifier, new Item(createSettings(identifier)));
    }

    private static void putDoll(String name) {
        var identifier = id(name);
        DOLLS.put(identifier, new Item(createSettings(identifier)));
    }

    public static void registerItems() {
        // Register stuff
        Registry.register(Registries.ITEM, id("porcelain"), PORCELAIN);
        Registry.register(Registries.ITEM, id("unfired_porcelain_crucible"), UNFIRED_PORCELAIN_CRUCIBLE);
        Registry.register(Registries.ITEM, id("salt_bottle"), SALT_BOTTLE);
        Registry.register(Registries.ITEM, id("raw_silkworm"), RAW_SILKWORM);
        Registry.register(Registries.ITEM, id("cooked_silkworm"), COOKED_SILKWORM);

        SEEDS.forEach((identifier, item) -> Registry.register(Registries.ITEM, identifier, item));
        MESHES.forEach((identifier, item) -> Registry.register(Registries.ITEM, identifier, item));
        PEBBLES.forEach((identifier, item) -> Registry.register(Registries.ITEM, identifier, item));
        DOLLS.forEach((identifier, item) -> Registry.register(Registries.ITEM, identifier, item));
        ORE_PIECES.forEach((identifier, item) -> Registry.register(Registries.ITEM, identifier, item));
        Registry.register(Registries.ITEM, id("end_cake"), new BlockItem(ModBlocks.END_CAKE, createSettings(id("end_cake")).maxCount(1)));
        ModFluids.registerBuckets();
    }

}
