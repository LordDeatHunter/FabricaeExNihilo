package wraith.fabricaeexnihilo.modules.sieves;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.MeshDefinition;
import wraith.fabricaeexnihilo.modules.base.Colored;
import wraith.fabricaeexnihilo.util.Color;

public class MeshItem extends Item implements Colored {
    protected static final FabricItemSettings ITEM_SETTINGS = new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP).maxCount(FabricaeExNihilo.CONFIG.modules.sieves.meshStackSize);
    
    private final Color color;
    private final int enchantability;
    
    public MeshItem(MeshDefinition definition) {
        super(ITEM_SETTINGS);
        this.color = definition.color();
        this.enchantability = definition.enchantability();
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