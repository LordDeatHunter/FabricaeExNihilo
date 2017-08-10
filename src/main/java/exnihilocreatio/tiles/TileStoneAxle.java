package exnihilocreatio.tiles;

import exnihilocreatio.rotationalPower.IRotationalPowerMember;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class TileStoneAxle extends BaseTileEntity implements ITickable, IRotationalPowerMember {
    public float rotationValue = 0;
    public float perTickEffective = 0F;

    public EnumFacing facing = EnumFacing.NORTH;

    private int counter = -1;
    @Override
    public void update() {
        counter++;

        if (counter % 10 == 0) {
            float lastPerTickEffective = perTickEffective;
            perTickEffective = calcEffectivePerTickRotation(facing);

            if (lastPerTickEffective != perTickEffective) {
                markDirty();
            }
        }

        if (world.isRemote){
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

    private float calcEffectivePerTickRotation(EnumFacing direction) {
        if (facing == direction) {
            BlockPos posProvider = pos.offset(facing.getOpposite());
            TileEntity te = world.getTileEntity(posProvider);
            if (te != null && te instanceof IRotationalPowerMember) {
                return ((IRotationalPowerMember) te).getEffectivePerTickRotation(facing) + getOwnRotation();
            } else return getOwnRotation();
        } else return 0;
    }

    @Override
    public void setEffectivePerTickRotation(float rotation) {
        perTickEffective = rotation;
    }
}
