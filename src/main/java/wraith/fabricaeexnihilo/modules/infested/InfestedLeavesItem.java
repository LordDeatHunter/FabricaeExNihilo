package wraith.fabricaeexnihilo.modules.infested;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import wraith.fabricaeexnihilo.modules.base.Colored;
import wraith.fabricaeexnihilo.util.Color;

public class InfestedLeavesItem extends BlockItem implements Colored {
    
    public InfestedLeavesItem(InfestedLeavesBlock block, FabricItemSettings settings) {
        super(block, settings);
    }
    
    @Override
    public int getColor(int index) {
        return Color.WHITE.toInt();
    }
}
