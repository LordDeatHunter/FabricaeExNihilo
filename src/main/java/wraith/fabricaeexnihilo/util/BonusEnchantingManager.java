package wraith.fabricaeexnihilo.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.RegistryKey;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ModTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class BonusEnchantingManager {
    public static final Map<RegistryKey<Enchantment>, List<Item>> DATA = new Object2ObjectOpenHashMap<>();

    private BonusEnchantingManager() {
    }

    public static void init() {
        generateDefaultTags();

        // Allow items to be enchanted with bonus enchantments.
        //
        // The upstream 1.21 branch did this through a mixin on
        // Enchantment#isAcceptableItem. That is no longer possible in 1.21.11:
        // Enchantment became a final record there, so the class cannot be
        // subclassed and the mixin cannot be applied at all. Fabric's own
        // ALLOW_ENCHANTING event is the supported route and does the same job.
        EnchantmentEvents.ALLOW_ENCHANTING.register((entry, stack, ctx) -> {
            if (entry == null || entry.getKey().isEmpty()) return TriState.DEFAULT;
            var key = entry.getKey().get();
            var items = BonusEnchantingManager.DATA.get(key);
            if (items == null || !items.contains(stack.getItem())) return TriState.DEFAULT;
            return TriState.TRUE;
        });
    }

    public static void generateDefaultTags() {
        if (FabricaeExNihilo.CONFIG.get().crucibles().efficiency()) {
            DATA.computeIfAbsent(Enchantments.EFFICIENCY, enchantment -> new ArrayList<>()).addAll(ModBlocks.CRUCIBLES.values().stream().map(ItemConvertible::asItem).toList());
        }
        if (FabricaeExNihilo.CONFIG.get().crucibles().fireAspect()) {
            DATA.computeIfAbsent(Enchantments.FIRE_ASPECT, enchantment -> new ArrayList<>()).addAll(ModBlocks.CRUCIBLES.values().stream().map(ItemConvertible::asItem).toList());
        }
        if (FabricaeExNihilo.CONFIG.get().barrels().efficiency()) {
            DATA.computeIfAbsent(Enchantments.EFFICIENCY, enchantment -> new ArrayList<>()).addAll(ModBlocks.BARRELS.values().stream().map(ItemConvertible::asItem).toList());
        }
        if (FabricaeExNihilo.CONFIG.get().barrels().thorns()) {
            DATA.computeIfAbsent(Enchantments.THORNS, enchantment -> new ArrayList<>()).addAll(ModBlocks.BARRELS.values().stream().map(ItemConvertible::asItem).toList());
        }
        if (FabricaeExNihilo.CONFIG.get().sieves().efficiency()) {
            DATA.computeIfAbsent(Enchantments.EFFICIENCY, enchantment -> new ArrayList<>()).addAll(ModItems.MESHES.values());
        }
        if (FabricaeExNihilo.CONFIG.get().sieves().fortune()) {
            DATA.computeIfAbsent(Enchantments.FORTUNE, enchantment -> new ArrayList<>()).addAll(ModItems.MESHES.values());
        }
        DATA.computeIfAbsent(Enchantments.EFFICIENCY, enchantment -> new ArrayList<>()).addAll(ModTools.HAMMERS.values());
        DATA.computeIfAbsent(Enchantments.EFFICIENCY, enchantment -> new ArrayList<>()).addAll(ModTools.CROOKS.values());
    }

    /**
     * Checks whether an item may carry one of the bonus enchantments.
     *
     * <p>The enchantment is matched by its registry key rather than by its
     * display name, so the lookup survives the 1.21.11 rewrite of Enchantment
     * into a record where the registry key is no longer reachable from the
     * instance itself.
     */
    public static boolean isBonusEnchantable(RegistryKey<Enchantment> key, Item item) {
        var items = DATA.get(key);
        return items != null && items.contains(item);
    }
}
