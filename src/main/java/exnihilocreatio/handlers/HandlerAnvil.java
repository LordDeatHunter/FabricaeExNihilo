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
    public void AnvilUpdate(AnvilUpdateEvent event){
        final ItemStack right = event.getRight();
        final ItemStack left = event.getLeft();
        if(!left.isEmpty() && left.getCount() == 1 &&
                !right.isEmpty() && right.getCount() == 1 &&
                left.getItem() == ModItems.mesh && left.isItemEqual(right)){
            ItemStack output = left.copy();
            Map<Enchantment, Integer> outputEnchs = EnchantmentHelper.getEnchantments(output);
            int cost = event.getCost();
            final Map<Enchantment, Integer> leftEnchs = EnchantmentHelper.getEnchantments(left);
            final Map<Enchantment, Integer> rightEnchs = EnchantmentHelper.getEnchantments(right);
            for(Enchantment ench : rightEnchs.keySet()){
                if(leftEnchs.containsKey(ench)){
                    int level = leftEnchs.get(ench);
                    if(level == rightEnchs.get(ench)){
                        level += 1;
                    }
                    else if(level < rightEnchs.get(ench)){
                        level = rightEnchs.get(ench);
                    }
                    outputEnchs.put(ench, level);
                    cost += 1 << level;
                }
                else {
                    outputEnchs.put(ench, rightEnchs.get(ench));
                    cost += 1 << rightEnchs.get(ench);
                }
            }
            EnchantmentHelper.setEnchantments(outputEnchs, output);
            event.setOutput(output);
            event.setCost(cost);
            event.setMaterialCost(1);
        }
    }
}
