package wraith.fabricaeexnihilo.compatibility.wthit;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.text.TranslatableText;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlockEntity;

public class CrucibleComponentProvider implements IBlockComponentProvider {
    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        CrucibleBlockEntity crucible = accessor.getBlockEntity();
        if (crucible == null) return;
        
        if (crucible.getQueued() > 0) {
            tooltip.addLine(new TranslatableText("fabricaeexnihilo.wthit.crucible.queued", crucible.getQueued() / 81 /* Convert to millibuckets */));
        }
    }
}
