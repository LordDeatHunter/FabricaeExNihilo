package wraith.fabricaeexnihilo.modules.base;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class EnchantableBlockItem extends BlockItem {

    private final int enchantability;

    public EnchantableBlockItem(Block block, Settings settings, int enchantability) {
        super(block, settings.enchantable(enchantability));
        this.enchantability = enchantability;
    }

    public int getEnchantability() {
        return enchantability;
    }

}
