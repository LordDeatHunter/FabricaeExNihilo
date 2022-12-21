package wraith.fabricaeexnihilo.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ModTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BonusEnchantingManager {
    public static final Map<Enchantment, List<Item>> DATA = new HashMap<>();

    private BonusEnchantingManager() {
    }

    public static void generateDefaultTags() {
        if (FabricaeExNihilo.CONFIG.modules.crucibles.efficiency) {
            DATA.computeIfAbsent(Enchantments.EFFICIENCY, enchantment -> new ArrayList<>()).addAll(ModBlocks.CRUCIBLES.values().stream().map(ItemConvertible::asItem).toList());
        }
        if (FabricaeExNihilo.CONFIG.modules.barrels.enchantments.efficiency) {
            DATA.computeIfAbsent(Enchantments.EFFICIENCY, enchantment -> new ArrayList<>()).addAll(ModBlocks.BARRELS.values().stream().map(ItemConvertible::asItem).toList());
        }
        if (FabricaeExNihilo.CONFIG.modules.sieves.efficiency) {
            DATA.computeIfAbsent(Enchantments.EFFICIENCY, enchantment -> new ArrayList<>()).addAll(ModItems.MESHES.values());
        }
        if (FabricaeExNihilo.CONFIG.modules.barrels.enchantments.thorns) {
            DATA.computeIfAbsent(Enchantments.THORNS, enchantment -> new ArrayList<>()).addAll(ModBlocks.BARRELS.values().stream().map(ItemConvertible::asItem).toList());
        }
        if (FabricaeExNihilo.CONFIG.modules.crucibles.fireAspect) {
            DATA.computeIfAbsent(Enchantments.FIRE_ASPECT, enchantment -> new ArrayList<>()).addAll(ModBlocks.CRUCIBLES.values().stream().map(ItemConvertible::asItem).toList());
        }
        if (FabricaeExNihilo.CONFIG.modules.sieves.fortune) {
            DATA.computeIfAbsent(Enchantments.FORTUNE, enchantment -> new ArrayList<>()).addAll(ModItems.MESHES.values());
        }
        DATA.computeIfAbsent(Enchantments.EFFICIENCY, enchantment -> new ArrayList<>()).addAll(ModTools.HAMMERS.values());
        DATA.computeIfAbsent(Enchantments.EFFICIENCY, enchantment -> new ArrayList<>()).addAll(ModTools.CROOKS.values());
    }

}
