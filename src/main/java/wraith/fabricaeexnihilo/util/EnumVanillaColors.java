package wraith.fabricaeexnihilo.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public enum EnumVanillaColors {
    WHITE,
    ORANGE,
    MAGENTA,
    LIGHT_BLUE,
    YELLOW,
    LIME,
    PINK,
    GRAY,
    LIGHT_GRAY,
    CYAN,
    PURPLE,
    BLUE,
    BROWN,
    GREEN,
    RED,
    BLACK;

    public Block getConcrete() {
        return Registry.BLOCK.get(new Identifier(name().toLowerCase() + "_concrete"));
    }

    public Block getConcretePowder() {
        return Registry.BLOCK.get(new Identifier(name().toLowerCase() + "_concrete_powder"));
    }

    public Block getWool() {
        return Registry.BLOCK.get(new Identifier(name().toLowerCase() + "_wool"));
    }

    public Item getDye() {
        return Registry.ITEM.get(new Identifier(name().toLowerCase() + "_dye"));
    }

    public Block getGlass() {
        return Registry.BLOCK.get(new Identifier(name().toLowerCase() + "_stained_glass"));
    }

    public Block getGlassPane() {
        return Registry.BLOCK.get(new Identifier(name().toLowerCase() + "_stained_glass_pane"));
    }

    public Block getTerracotta() {
        return Registry.BLOCK.get(new Identifier(name().toLowerCase() + "_terracotta"));
    }

    public Block getGlazedTerracotta() {
        return Registry.BLOCK.get(new Identifier(name().toLowerCase() + "_glazed_terracotta"));
    }

    public Block getCarpet() {
        return Registry.BLOCK.get(new Identifier(name().toLowerCase() + "_carpet"));
    }

}