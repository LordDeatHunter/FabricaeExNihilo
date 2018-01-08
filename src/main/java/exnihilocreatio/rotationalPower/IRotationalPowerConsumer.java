package exnihilocreatio.rotationalPower;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    @Override
    default float calcEffectivePerTickRotation(World world, BlockPos pos, EnumFacing facing) {
        return -IRotationalPowerMember.super.calcEffectivePerTickRotation(world, pos, facing.getOpposite());
    }
}
