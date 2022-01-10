package wraith.fabricaeexnihilo.modules.ores;

import wraith.fabricaeexnihilo.api.ore.OreMaterial;
import wraith.fabricaeexnihilo.api.ore.OreShape;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.base.ColoredItem;
import wraith.fabricaeexnihilo.util.Color;

public class OreItem extends ColoredItem {
    private final OreShape shape;
    private final OreMaterial material;
    
    public OreItem(Color color, OreMaterial material, OreShape shape) {
        super(color, ModItems.BASE_SETTINGS);
        this.material = material;
        this.shape = shape;
    }
    
    public OreShape getShape() {
        return shape;
    }
    
    public OreMaterial getMaterial() {
        return material;
    }
    
}
