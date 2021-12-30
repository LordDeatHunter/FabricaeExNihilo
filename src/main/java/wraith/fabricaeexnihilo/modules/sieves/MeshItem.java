package wraith.fabricaeexnihilo.modules.sieves;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.base.Colored;
import wraith.fabricaeexnihilo.util.Color;

public class MeshItem extends Item implements Colored {

    private final Color color;
    private final int enchantability;
    
    public MeshItem(Color color, int enchantability, FabricItemSettings settings) {
        super(settings);
        this.color = color;
        this.enchantability = enchantability;
    }

    public MeshItem(Color color, int enchantability) {
        this(color, enchantability, ITEM_SETTINGS);
    }

    protected static final FabricItemSettings ITEM_SETTINGS = new FabricItemSettings().group(FabricaeExNihilo.ITEM_GROUP).maxCount(FabricaeExNihilo.CONFIG.modules.sieves.meshStackSize);

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