package wraith.fabricaeexnihilo.modules.ores;

import wraith.fabricaeexnihilo.api.OreDefinition;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.base.ColoredItem;

public class OreItem extends ColoredItem {
    private final OreDefinition definition;
    
    public OreItem(OreDefinition definition) {
        super(definition.color(), ModItems.BASE_SETTINGS);
        this.definition = definition;
    }
    
    public OreDefinition getDefinition() {
        return definition;
    }
}
