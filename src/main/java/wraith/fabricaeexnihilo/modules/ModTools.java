package wraith.fabricaeexnihilo.modules;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
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
            var crook = new CrookItem(toolMaterial.getMaterial(), ModItems.createSettings(crookId));
            CROOKS.put(crookId, crook);
        }
        putHammer("wooden_hammer", ToolMaterial.WOOD);
        putHammer("stone_hammer", ToolMaterial.STONE);
        putHammer("iron_hammer", ToolMaterial.IRON);
        putHammer("golden_hammer", ToolMaterial.GOLD);
        putHammer("diamond_hammer", ToolMaterial.DIAMOND);
        putHammer("netherite_hammer", ToolMaterial.NETHERITE);
    }

    private static void putHammer(String name, ToolMaterial material) {
        var identifier = id(name);
        HAMMERS.put(identifier, new HammerItem(material, ModItems.createSettings(identifier)));
    }

    public static void registerItems() {
        CROOKS.forEach((identifier, item) -> Registry.register(Registries.ITEM, identifier, item));
        HAMMERS.forEach((identifier, item) -> Registry.register(Registries.ITEM, identifier, item));
    }

}
