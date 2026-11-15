package wraith.fabricaeexnihilo.compatibility.wthit;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.text.Text;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlockEntity;

public class CrucibleComponentProvider implements IBlockComponentProvider {
    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        CrucibleBlockEntity crucible = accessor.getBlockEntity();
        if (crucible == null) return;

        // Keep this in step with the Jade provider: the solid is reported for as long as the
        // crucible still holds something it came from, not just while it is still melting.
        var stack = crucible.getDisplayStack();
        if (!stack.isEmpty()) {
            tooltip.addLine(Text.translatable("fabricaeexnihilo.hud.crucible.contents",
                    stack.getName(), stack.getCount()));
        }
        if (crucible.getQueued() > 0) {
            tooltip.addLine(Text.translatable("fabricaeexnihilo.hud.crucible.queued", crucible.getQueued() / 81 /* Convert to millibuckets */));
        }
    }
}
