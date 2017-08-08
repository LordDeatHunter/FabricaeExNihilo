package exnihilocreatio.rotationalPower;

public interface IRotationalPowerMember {

    float getOwnRotation();

    float getEffectivePerTickRotation();

    void setEffectivePerTickRotation(float rotation);

    default void addEffectiveRotation(float rotation){
        setEffectivePerTickRotation(getOwnRotation() + rotation);
    }
}
