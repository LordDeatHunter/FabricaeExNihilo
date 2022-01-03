package wraith.fabricaeexnihilo.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface WoodDefinition {
    Identifier getTexturePlanks();
    
    Identifier getTextureLog();
    
    Identifier getTextureLogTop();
    
    Identifier getLeafID();
    
    Identifier getLogID();
    
    Identifier getPlanksID();
    
    Identifier getSlabID();
    
    Identifier getSaplingID();
    
    default Block getLeafBlock() {
        return Registry.BLOCK.get(getLeafID());
    }
    
    default Block getLogBlock() {
        return Registry.BLOCK.get(getLogID());
    }
    
    default Block getPlanksBlock() {
        return Registry.BLOCK.get(getPlanksID());
    }
    
    default Block getSlabBlock() {
        return Registry.BLOCK.get(getSlabID());
    }
    
    default Block getSaplingBlock() {
        return Registry.BLOCK.get(getSaplingID());
    }
    
    // Used for Fabricae Ex Nihilo tree seeds
    Identifier getSeedID();
    
    default Item getSeedItem() {
        return Registry.ITEM.get(getSeedID());
    }
    
}