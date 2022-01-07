package wraith.fabricaeexnihilo.modules.base;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class EnchantableBlockItem extends BlockItem {
    
    private final int enchantability;
    
    public EnchantableBlockItem(Block block, Settings settings, int enchantability) {
        super(block, settings);
        this.enchantability = enchantability;
    }
    
    @Override
    public int getEnchantability() {
        return enchantability;
    }
    
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
    
}
