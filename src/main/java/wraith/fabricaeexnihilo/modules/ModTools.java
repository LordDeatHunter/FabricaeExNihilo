package wraith.fabricaeexnihilo.modules;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.materials.ModToolMaterials;
import wraith.fabricaeexnihilo.modules.tools.CrookTool;
import wraith.fabricaeexnihilo.modules.tools.HammerTool;

import java.util.HashMap;
import java.util.Map;

public class ModTools {
    public static final FabricItemSettings tool_settings = new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP).maxCount(1);

    public static final Map<Identifier, Item> CROOKS = new HashMap<>();
    public static final Map<Identifier, Item> HAMMERS = new HashMap<>();

    static {
        for (var toolMaterial : ModToolMaterials.values()) {
            var identifier = FabricaeExNihilo.ID("crook_" + toolMaterial.name().toLowerCase());
            var item = new CrookTool(toolMaterial, new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP).maxCount(1).maxDamage(toolMaterial.getDurability()));
            CROOKS.put(identifier, item);
        }

        HAMMERS.put(FabricaeExNihilo.ID("hammer_wood"), new HammerTool(ToolMaterials.WOOD, tool_settings));
        HAMMERS.put(FabricaeExNihilo.ID("hammer_stone"), new HammerTool(ToolMaterials.STONE, tool_settings));
        HAMMERS.put(FabricaeExNihilo.ID("hammer_iron"), new HammerTool(ToolMaterials.IRON, tool_settings));
        HAMMERS.put(FabricaeExNihilo.ID("hammer_gold"), new HammerTool(ToolMaterials.GOLD, tool_settings));
        HAMMERS.put(FabricaeExNihilo.ID("hammer_diamond"), new HammerTool(ToolMaterials.DIAMOND, tool_settings));
    }

    public static void registerItems() {
        CROOKS.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
        HAMMERS.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
    }

}
