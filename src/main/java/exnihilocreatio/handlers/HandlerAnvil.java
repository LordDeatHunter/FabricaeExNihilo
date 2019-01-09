package exnihilocreatio.handlers;

import exnihilocreatio.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class HandlerAnvil {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void anvilUpdate(AnvilUpdateEvent event) {
        final ItemStack right = event.getRight();
        final ItemStack left = event.getLeft();
        if (!left.isEmpty() && left.getCount() == 1 &&
                !right.isEmpty() && right.getCount() == 1 &&
                left.getItem() == ModItems.mesh && left.isItemEqual(right)) {
            ItemStack output = left.copy();
            Map<Enchantment, Integer> outputEnchs = EnchantmentHelper.getEnchantments(output);
            int cost = event.getCost();
            final Map<Enchantment, Integer> leftEnchs = EnchantmentHelper.getEnchantments(left);
            final Map<Enchantment, Integer> rightEnchs = EnchantmentHelper.getEnchantments(right);
            for (Map.Entry<Enchantment, Integer> ench : rightEnchs.entrySet()) {
                if (leftEnchs.containsKey(ench.getKey())) {
                    int level = leftEnchs.get(ench.getKey());
                    if (level == ench.getValue()) {
                        level += 1;
                    } else if (level < ench.getValue()) {
                        level = ench.getValue();
                    }
                    outputEnchs.put(ench.getKey(), level);
                    cost += 1 << level;
                } else {
                    outputEnchs.put(ench.getKey(), ench.getValue());
                    cost += 1 << ench.getValue();
                }
            }
            EnchantmentHelper.setEnchantments(outputEnchs, output);
            event.setOutput(output);
            event.setCost(cost);
            event.setMaterialCost(1);
        }
    }
}
