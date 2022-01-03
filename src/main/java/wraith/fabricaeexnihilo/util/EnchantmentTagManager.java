package wraith.fabricaeexnihilo.util;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.tags.JTag;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ModTags;

import java.util.HashMap;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class EnchantmentTagManager {
    
    private EnchantmentTagManager() {
    }
    
    public static List<EnchantmentLevelEntry> getHighestApplicableEnchantmentsAtPower(int power, ItemStack stack, boolean hasTreasure) {
        return getApplicableEnchantments(stack, hasTreasure).stream().map(enchantmentLevelEntry -> getHighestLevelAtPower(enchantmentLevelEntry, power)).toList();
    }
    
    
    public static Identifier getTagIDForEnchantment(Enchantment enchantment) {
        var enchID = RegistryUtils.getId(enchantment);
        if (enchID == null) {
            enchID = new Identifier("empty");
        }
        return id("enchantments/" + enchID.getNamespace() + "/" + enchID.getPath());
    }
    
    public static boolean itemIsAcceptableForEnchantment(ItemStack stack, Enchantment enchantment) {
        return TagFactory.ITEM.create(getTagIDForEnchantment(enchantment)).contains(stack.getItem());
    }
    
    private static Tag.Identified<Item> getTagForEnchantment(Enchantment enchantment) {
        return TagFactory.ITEM.create(getTagIDForEnchantment(enchantment));
    }
    
    /**
     * @return A list of enchantments for which this item is tagged with the item tag
     * fabricaeexnihilo:enchantments/modid/enchantment_id
     */
    public static List<Enchantment> getApplicableEnchantments(ItemStack stack, boolean hasTreasure) {
        return Registry.ENCHANTMENT.stream()
                .filter(enchantment -> hasTreasure || !enchantment.isTreasure())
                .filter(enchantment -> getTagForEnchantment(enchantment).contains(stack.getItem()))
                .toList();
    }
    
    public static void generateDefaultTags(RuntimeResourcePack resourcePack) {
        
        JTag tag = JTag.tag();
        if (FabricaeExNihilo.CONFIG.modules.crucibles.efficiency) {
            ModTags.addAllTags(tag, ModBlocks.CRUCIBLES.keySet());
        }
        if (FabricaeExNihilo.CONFIG.modules.barrels.efficiency) {
            ModTags.addAllTags(tag, ModBlocks.BARRELS.keySet());
        }
        if (FabricaeExNihilo.CONFIG.modules.sieves.efficiency) {
            ModTags.addAllTags(tag, ModItems.MESHES.keySet());
        }
        resourcePack.addTag(id("items/" + getTagIDForEnchantment(Enchantments.EFFICIENCY).getPath()), tag);
        
        tag = JTag.tag();
        if (FabricaeExNihilo.CONFIG.modules.barrels.thorns) {
            ModTags.addAllTags(tag, ModBlocks.BARRELS.keySet());
        }
        resourcePack.addTag(id("items/" + getTagIDForEnchantment(Enchantments.THORNS).getPath()), tag);
        
        tag = JTag.tag();
        if (FabricaeExNihilo.CONFIG.modules.crucibles.fireAspect) {
            ModTags.addAllTags(tag, ModBlocks.CRUCIBLES.keySet());
        }
        resourcePack.addTag(id("items/" + getTagIDForEnchantment(Enchantments.FIRE_ASPECT).getPath()), tag);
        
        tag = JTag.tag();
        if (FabricaeExNihilo.CONFIG.modules.sieves.fortune) {
            ModTags.addAllTags(tag, ModItems.MESHES.keySet());
        }
        resourcePack.addTag(id("items/" + getTagIDForEnchantment(Enchantments.FORTUNE).getPath()), tag);
    }
    
    public static List<EnchantmentLevelEntry> mergeInfoLists(List<EnchantmentLevelEntry> first, List<EnchantmentLevelEntry> second) {
        var mapping = new HashMap<Enchantment, Integer>();
        first.forEach(enchantment -> mapping.put(enchantment.enchantment, Math.max(mapping.getOrDefault(enchantment.enchantment, 0), enchantment.level)));
        second.forEach(enchantment -> mapping.put(enchantment.enchantment, Math.max(mapping.getOrDefault(enchantment.enchantment, 0), enchantment.level)));
        return mapping.entrySet().stream().map(entry -> new EnchantmentLevelEntry(entry.getKey(), entry.getValue())).toList();
    }
    
    public static EnchantmentLevelEntry getHighestLevelAtPower(Enchantment enchantment, int power) {
        for (var level = enchantment.getMaxLevel(); level >= enchantment.getMinLevel() - 1; level--) {
            if (power >= enchantment.getMinPower(level) && power <= enchantment.getMaxPower(level)) {
                return new EnchantmentLevelEntry(enchantment, level);
            }
        }
        return new EnchantmentLevelEntry(enchantment, 1);
    }
    
}
