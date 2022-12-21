package wraith.fabricaeexnihilo.compatibility.wthit;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.text.Text;
import wraith.fabricaeexnihilo.modules.infested.InfestingLeavesBlockEntity;

public class InfestingLeavesProvider implements IBlockComponentProvider {
    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        InfestingLeavesBlockEntity leaves = accessor.getBlockEntity();
        if (leaves == null) return;

        tooltip.addLine(Text.translatable("fabricaeexnihilo.hud.infesting_leaves.progress", (int) (leaves.getProgress() * 100)));
    }
}
