package exnihilocreatio.rotationalPower;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRotationalPowerMember {

    float getOwnRotation();

    /**
     * Gets the speed the PowerMember is spinning at
     * used to calcualte the speed of the member infront of him
     *
     * @param side is the side where he outputs power to
     * @return Returns the rotation per tick based on side
     */
    float getEffectivePerTickRotation(EnumFacing side);

    void setEffectivePerTickRotation(float rotation);

    default void addEffectiveRotation(float rotation) {
        setEffectivePerTickRotation(getOwnRotation() + rotation);
    }

    default float calcEffectivePerTickRotation(World world, BlockPos pos, EnumFacing facing) {
        BlockPos offset = pos.offset(facing.getOpposite());
        IRotationalPowerMember m = RotationalUtils.getPowerMember(world, offset, facing);
        if (m != null)
            return m.getEffectivePerTickRotation(facing) + getOwnRotation();
        return getOwnRotation();
    }
}
