package wraith.fabricaeexnihilo.modules.sieves;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.base.Colored;
import wraith.fabricaeexnihilo.util.Color;

public class MeshItem extends Item implements Colored {
    private final Color color;
    private final int enchantability;
    
    public MeshItem(Color color, int enchantability, Settings settings) {
        super(settings.maxCount(FabricaeExNihilo.CONFIG.modules.sieves.meshStackSize));
        this.color = color;
        this.enchantability = enchantability;
    }
    
    @Override
    public int getColor(int index) {
        return color.toInt();
    }
    
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
    
    @Override
    public int getEnchantability() {
        return this.enchantability;
    }
    
    public Text getName(ItemStack stack) {
        return getName();
    }
    
}