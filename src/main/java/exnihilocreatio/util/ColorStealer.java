package exnihilocreatio.util;

import exnihilocreatio.texturing.Color;
import mezz.jei.color.ColorGetter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import java.util.HashMap;
import java.util.List;

/**
 * TODO: remove dep on JEI
 */
public class ColorStealer {
    public static HashMap<ItemInfo, Color> colorCache = new HashMap<>();

    public static Color getColor(ItemStack stack) {
        if (stack.isEmpty()) return Color.INVALID_COLOR;
        ItemInfo info = new ItemInfo(stack);

        if (colorCache.containsKey(info)) {
            return colorCache.get(info);
        } else if (Loader.isModLoaded("jei")) {
            List<java.awt.Color> color = ColorGetter.getColors(stack, 1);
            if (color.size() > 0){
                java.awt.Color domColor = color.get(0);
                Color exColor = new Color(domColor.getRGB());
                colorCache.put(info, exColor);

                return exColor;
            }
        } else if (stack.getItem() instanceof ItemBlock){
            return getColorBlock(stack, ((ItemBlock) stack.getItem()).getBlock());
        } else {
            return getColorItem(stack);
        }

        return Color.INVALID_COLOR;
    }


    private static Color getColorBlock(ItemStack item, Block block){
        return Color.INVALID_COLOR;
    }

    private static Color getColorItem(ItemStack stack){
        return Color.INVALID_COLOR;
    }
}
