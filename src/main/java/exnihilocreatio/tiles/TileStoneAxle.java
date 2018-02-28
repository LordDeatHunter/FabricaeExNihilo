package exnihilocreatio.tiles;

import exnihilocreatio.rotationalPower.CapabilityRotationalMember;
import exnihilocreatio.rotationalPower.IRotationalPowerMember;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileStoneAxle extends BaseTileEntity implements ITickable, IRotationalPowerMember {
    public float rotationValue = 0;
    public float perTickEffective = 0F;

    public EnumFacing facing = EnumFacing.NORTH;

    private int counter = -1;

    @Override
    public void update() {
        counter++;

        if (counter > 0 && counter % 10 == 0) {
            float lastPerTickEffective = perTickEffective;
            perTickEffective = calcEffectivePerTickRotation(world, pos, facing);

            if (lastPerTickEffective != perTickEffective) {
                markDirty();
            }
            counter = 0;
        }

        if (world.isRemote) {
            rotationValue += perTickEffective;
        }
    }


    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (facing != null)
            tag.setString("facing", facing.getName());
        tag.setFloat("rot", rotationValue);

        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("facing"))
            facing = EnumFacing.byName(tag.getString("facing"));
        if (tag.hasKey("rot"))
            rotationValue = tag.getFloat("rot");
    }

    @Override
    public float getOwnRotation() {
        return 0;
    }

    @Override
    public float getEffectivePerTickRotation(EnumFacing direction) {
        if (facing == direction) {
            return perTickEffective;
        } else {
            return 0;
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityRotationalMember.ROTIONAL_MEMBER && facing == this.facing || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityRotationalMember.ROTIONAL_MEMBER && facing == this.facing)
            return CapabilityRotationalMember.ROTIONAL_MEMBER.cast(this);
        return super.getCapability(capability, facing);
    }

    @Override
    public void setEffectivePerTickRotation(float rotation) {
        perTickEffective = rotation;
    }
}
