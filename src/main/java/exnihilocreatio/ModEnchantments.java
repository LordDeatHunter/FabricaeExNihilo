package exnihilocreatio;

import exnihilocreatio.enchantments.EnchantmentEfficiency;
import exnihilocreatio.enchantments.EnchantmentFortune;
import exnihilocreatio.enchantments.EnchantmentLuckOfTheSea;
import exnihilocreatio.util.Data;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.registries.IForgeRegistry;

public class ModEnchantments {

    public static final EnchantmentEfficiency EFFICIENCY = new EnchantmentEfficiency();
    public static final EnchantmentFortune FORTUNE = new EnchantmentFortune();
    public static final EnchantmentLuckOfTheSea LUCK_OF_THE_SEA = new EnchantmentLuckOfTheSea();

    public static void registerEnchantments(IForgeRegistry<Enchantment> registry) {
        for (Enchantment enchantment : Data.ENCHANTMENTS) {
            registry.register(enchantment);
        }
    }

}
