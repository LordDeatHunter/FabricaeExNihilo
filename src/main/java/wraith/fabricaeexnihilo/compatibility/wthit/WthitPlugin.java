package wraith.fabricaeexnihilo.compatibility.wthit;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlock;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlock;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlock;

public class WthitPlugin implements IWailaPlugin {
    @Override
    public void register(IRegistrar registrar) {
        registrar.addComponent(new BarrelComponentProvider(), TooltipPosition.BODY, BarrelBlock.class);
        registrar.addComponent(new CrucibleComponentProvider(), TooltipPosition.BODY, CrucibleBlock.class);
        registrar.addComponent(new SieveComponentProvider(), TooltipPosition.BODY, SieveBlock.class);
    }
}
