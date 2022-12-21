package wraith.fabricaeexnihilo.compatibility.theoneprobe;

import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ITheOneProbePlugin;

public class TheOneProbePlugin implements ITheOneProbePlugin {
    @Override
    public void onLoad(ITheOneProbe api) {
        api.registerProvider(new BarrelProbeInfoProvider());
        api.registerProvider(new CrucibleProbeInfoProvider());
        api.registerProvider(new SieveProbeInfoProvider());
        api.registerProvider(new InfestingLeavesProvider());
    }

}
