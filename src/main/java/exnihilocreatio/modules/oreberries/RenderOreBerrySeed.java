package exnihilocreatio.modules.oreberries;

import josephcsible.oreberries.BlockOreberryBush;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class RenderOreBerrySeed implements IItemColor {
    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        if (stack.isEmpty())
            return 0;

        if (stack.getItem() instanceof ItemOreBerrySeed && tintIndex == 1) {
             BlockOreberryBush bush = ((ItemOreBerrySeed)stack.getItem()).getBush();
             return (new exnihilocreatio.texturing.Color(bush.config.color.substring(1))).toInt();
        }
        return Color.WHITE.getRGB();
    }
}
