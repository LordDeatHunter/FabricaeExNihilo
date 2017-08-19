package exnihilocreatio.rotationalPower;

import net.minecraft.util.EnumFacing;

public interface IRotationalPowerConsumer extends IRotationalPowerMember {
    @Override
    default float getOwnRotation() {
        return 0;
    }

    @Override
    default float getEffectivePerTickRotation(EnumFacing side) {
        return 0;
    }

    float getMachineRotationPerTick();
}
