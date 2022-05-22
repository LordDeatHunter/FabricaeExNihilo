package wraith.fabricaeexnihilo.compatibility.wthit;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.text.TranslatableText;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.modes.AlchemyMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.CompostMode;

public class BarrelComponentProvider implements IBlockComponentProvider {
    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        BarrelBlockEntity barrel = accessor.getBlockEntity();
        if (barrel == null) return;
    
        var mode = barrel.getMode();
        if (mode instanceof CompostMode compostMode) {
            if (compostMode.getAmount() < 1) {
                tooltip.addLine(new TranslatableText("fabricaeexnihilo.wthit.barrel.compost.filling", (int)(compostMode.getAmount() * 100)));
            } else {
                tooltip.addLine(new TranslatableText("fabricaeexnihilo.wthit.barrel.compost.composting", (int)(compostMode.getProgress() * 100)));
            }
        } else if (mode instanceof AlchemyMode alchemyMode) {
            tooltip.addLine(new TranslatableText("fabricaeexnihilo.wthit.barrel.alchemy.processing", (int)((100.0 / alchemyMode.getDuration()) * alchemyMode.getProgress())));
        }
    }
}
