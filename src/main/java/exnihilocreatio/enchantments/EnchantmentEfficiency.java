package exnihilocreatio.enchantments;

import exnihilocreatio.items.ItemMesh;
import exnihilocreatio.util.Data;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentEfficiency extends Enchantment {

    public EnchantmentEfficiency() {
        super(Rarity.COMMON, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setName("sieve_efficiency");
        this.setRegistryName("sieve_efficiency");

        Data.ENCHANTMENTS.add(this);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof ItemMesh;
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + 10 * (enchantmentLevel - 1);
    }

    /**
     * Returns the maximum value of enchantability needed on the enchantment level passed.
     */
    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    @Override
    public int getMaxLevel() {
        return 5;
    }
}
