package wraith.fabricaeexnihilo.modules.ore;

import net.minecraft.item.Item;
import wraith.fabricaeexnihilo.modules.base.Colored;
import wraith.fabricaeexnihilo.util.Color;

public class ColoredItem extends Item implements Colored {
    
    private final Color color;

    public ColoredItem(OreProperties properties, Settings settings) {
        super(settings);
        this.color = properties.getColor();
    }
    
    public ColoredItem(Color color, Settings settings) {
        super(settings);
        this.color = color;
    }

    @Override
    public int getColor(int index) {
        return index == 1 ? color.toInt() : Color.WHITE.toInt();
    }
}
