package exnihilocreatio.rotationalPower;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;

public class CapabilityRotationalMember implements Capability.IStorage<IRotationalPowerMember>, Callable<IRotationalPowerMember> {

    @SuppressWarnings("CanBeFinal")
    @CapabilityInject(IRotationalPowerMember.class)
    public static Capability<IRotationalPowerMember> ROTIONAL_MEMBER = null;

    public static final CapabilityRotationalMember INSTANCE = new CapabilityRotationalMember();

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IRotationalPowerMember> capability, IRotationalPowerMember instance, EnumFacing side) {
        return null;
    }

    @Override
    public void readNBT(Capability<IRotationalPowerMember> capability, IRotationalPowerMember instance, EnumFacing side, NBTBase nbt) {

    }

    @Override
    public IRotationalPowerMember call() throws Exception {
        return new IRotationalPowerMember() {

            @Override
            public float getOwnRotation() {
                return 0;
            }

            @Override
            public float getEffectivePerTickRotation(EnumFacing side) {
                return 0;
            }

            @Override
            public void setEffectivePerTickRotation(float rotation) {

            }
        };
    }
}
