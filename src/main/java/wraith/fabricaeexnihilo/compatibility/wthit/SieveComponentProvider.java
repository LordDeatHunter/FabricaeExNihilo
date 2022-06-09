package wraith.fabricaeexnihilo.compatibility.wthit;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.text.TranslatableText;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlockEntity;

public class SieveComponentProvider implements IBlockComponentProvider {
    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        SieveBlockEntity sieve = accessor.getBlockEntity();
        if (sieve == null) return;
    
        if (!sieve.getMesh().isEmpty()) {
            tooltip.addLine(new TranslatableText("fabricaeexnihilo.hud.sieve.mesh", sieve.getMesh().getName()));
        }
        if (!sieve.getContents().isEmpty()) {
            tooltip.addLine(new TranslatableText("fabricaeexnihilo.hud.sieve.content", sieve.getContents().getName()));
            tooltip.addLine(new TranslatableText("fabricaeexnihilo.hud.sieve.progress", (int) (sieve.getProgress() * 100)));
        }
    }
}
