package wraith.fabricaeexnihilo.util;

import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.WoodDefinition;

public enum VanillaWoodDefinitions implements WoodDefinition {
    OAK("oak"),
    SPRUCE("spruce"),
    BIRCH("birch"),
    JUNGLE("jungle"),
    ACACIA("acacia"),
    DARK_OAK("dark_oak");
    
    private final String text;
    
    VanillaWoodDefinitions(String text) {
        this.text = text;
    }
    
    public Identifier getTexturePlanks() {
        return new Identifier("block/" + text + "_planks");
    }
    
    public Identifier getTextureLog() {
        return new Identifier("block/" + text + "_log");
    }
    
    public Identifier getTextureLogTop() {
        return new Identifier("block/" + text + "_log_top");
    }
    
    public Identifier getLeafID() {
        return new Identifier(text + "_leaves");
    }
    
    public Identifier getLogID() {
        return new Identifier(text + "_log");
    }
    
    public Identifier getPlanksID() {
        return new Identifier(text + "_planks");
    }
    
    public Identifier getSlabID() {
        return new Identifier(text + "_slab");
    }
    
    public Identifier getSaplingID() {
        return new Identifier(text + "_sapling");
    }
    
    public Identifier getSeedID() {
        return FabricaeExNihilo.id("seed_" + text);
    }
    
}
