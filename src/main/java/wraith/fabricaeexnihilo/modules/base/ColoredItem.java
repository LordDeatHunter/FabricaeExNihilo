package wraith.fabricaeexnihilo.modules.base;

import net.minecraft.item.Item;
import wraith.fabricaeexnihilo.util.Color;

public class ColoredItem extends Item implements Colored {
    
    private final Color color;
    
    public ColoredItem(Color color, Settings settings) {
        super(settings);
        this.color = color;
    }
    
    @Override
    public int getColor(int index) {
        return index == 1 ? color.toInt() : Color.WHITE.toInt();
    }
}
