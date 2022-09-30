package wraith.fabricaeexnihilo.modules;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.materials.ModToolMaterials;
import wraith.fabricaeexnihilo.modules.tools.CrookItem;
import wraith.fabricaeexnihilo.modules.tools.HammerItem;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

// TODO: merge with ModItems (why is this a thing?)
public class ModTools {

    public static final Map<Identifier, Item> CROOKS = new HashMap<>();
    public static final Map<Identifier, Item> HAMMERS = new HashMap<>();

    static {
        for (var toolMaterial : ModToolMaterials.values()) {
            var crookId = id(toolMaterial.name().toLowerCase(Locale.ROOT) + "_crook");
            var crook = new CrookItem(toolMaterial, new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP));
            CROOKS.put(crookId, crook);
        }
        HAMMERS.put(id("wooden_hammer"), new HammerItem(ToolMaterials.WOOD, new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP)));
        HAMMERS.put(id("stone_hammer"), new HammerItem(ToolMaterials.STONE, new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP)));
        HAMMERS.put(id("iron_hammer"), new HammerItem(ToolMaterials.IRON, new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP)));
        HAMMERS.put(id("golden_hammer"), new HammerItem(ToolMaterials.GOLD, new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP)));
        HAMMERS.put(id("diamond_hammer"), new HammerItem(ToolMaterials.DIAMOND, new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP)));
        HAMMERS.put(id("netherite_hammer"), new HammerItem(ToolMaterials.NETHERITE, new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP)));
    }

    public static void registerItems() {
        CROOKS.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
        HAMMERS.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
    }

}
