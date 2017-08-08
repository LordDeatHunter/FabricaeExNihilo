package exnihilocreatio.rotationalPower;

import net.minecraft.util.EnumFacing;

public interface IRotationalPowerMember {

    float getOwnRotation();

    /**
     * Gets the speed the PowerMember is spinning at
     * used to calcualte the speed of the member infront of him
     * @param side is the side where he outputs power to
     * @return
     */
    float getEffectivePerTickRotation(EnumFacing side);

    void setEffectivePerTickRotation(float rotation);

    default void addEffectiveRotation(float rotation){
        setEffectivePerTickRotation(getOwnRotation() + rotation);
    }
}
