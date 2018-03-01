package exnihilocreatio.capabilities;

import exnihilocreatio.rotationalPower.CapabilityRotationalMember;
import exnihilocreatio.rotationalPower.IRotationalPowerMember;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ENCapabilities {
    public static void init() {
        CapabilityManager.INSTANCE.register(ICapabilityHeat.class, CapabilityHeatManager.INSTANCE, CapabilityHeatManager.INSTANCE);
        CapabilityManager.INSTANCE.register(IRotationalPowerMember.class, CapabilityRotationalMember.INSTANCE, CapabilityRotationalMember.INSTANCE);
    }
}
