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
import java.util.Map;

public class ModTools {
    public static final FabricItemSettings tool_settings = new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP).maxCount(1);

    public static final Map<Identifier, Item> CROOKS = new HashMap<>();
    public static final Map<Identifier, Item> HAMMERS = new HashMap<>();

    static {
        for (var toolMaterial : ModToolMaterials.values()) {
            var identifier = FabricaeExNihilo.id("crook_" + toolMaterial.name().toLowerCase());
            var item = new CrookItem(toolMaterial, new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP).maxCount(1).maxDamage(toolMaterial.getDurability()));
            CROOKS.put(identifier, item);
        }

        HAMMERS.put(FabricaeExNihilo.id("hammer_wood"), new HammerItem(ToolMaterials.WOOD, tool_settings));
        HAMMERS.put(FabricaeExNihilo.id("hammer_stone"), new HammerItem(ToolMaterials.STONE, tool_settings));
        HAMMERS.put(FabricaeExNihilo.id("hammer_iron"), new HammerItem(ToolMaterials.IRON, tool_settings));
        HAMMERS.put(FabricaeExNihilo.id("hammer_gold"), new HammerItem(ToolMaterials.GOLD, tool_settings));
        HAMMERS.put(FabricaeExNihilo.id("hammer_diamond"), new HammerItem(ToolMaterials.DIAMOND, tool_settings));
    }

    public static void registerItems() {
        CROOKS.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
        HAMMERS.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
    }

}
