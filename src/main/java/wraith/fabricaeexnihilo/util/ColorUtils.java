package wraith.fabricaeexnihilo.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ColorUtils {
    public static Block getConcrete(DyeColor color) {
        return Registry.BLOCK.get(new Identifier(color.asString() + "_concrete"));
    }
    
    public static Block getConcretePowder(DyeColor color) {
        return Registry.BLOCK.get(new Identifier(color.asString() + "_concrete_powder"));
    }
    
    public static Block getWool(DyeColor color) {
        return Registry.BLOCK.get(new Identifier(color.asString() + "_wool"));
    }
    
    public static Item getDye(DyeColor color) {
        return Registry.ITEM.get(new Identifier(color.asString() + "_dye"));
    }
    
    public static Block getGlass(DyeColor color) {
        return Registry.BLOCK.get(new Identifier(color.asString() + "_stained_glass"));
    }
    
    public static Block getGlassPane(DyeColor color) {
        return Registry.BLOCK.get(new Identifier(color.asString() + "_stained_glass_pane"));
    }
    
    public static Block getTerracotta(DyeColor color) {
        return Registry.BLOCK.get(new Identifier(color.asString() + "_terracotta"));
    }
    
    public static Block getGlazedTerracotta(DyeColor color) {
        return Registry.BLOCK.get(new Identifier(color.asString() + "_glazed_terracotta"));
    }
    
    public static Block getCarpet(DyeColor color) {
        return Registry.BLOCK.get(new Identifier(color.asString() + "_carpet"));
    }
    
}