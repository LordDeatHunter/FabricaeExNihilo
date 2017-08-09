package exnihilocreatio.tiles;

import exnihilocreatio.rotationalPower.IRotationalPowerMember;
import exnihilocreatio.rotationalPower.IRotationalPowerProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class TileWaterwheel extends BaseTileEntity implements ITickable, IRotationalPowerProvider {
    public static final float ROTATION_PER_WHEEL = 0.5F;

    public float rotationValue = 0;
    public float perTickRotationOwn = 0F;
    public float perTickEffective = 0F;

    public EnumFacing facing = EnumFacing.NORTH;

    public boolean canTurn = true;

    private int counter = -1;

    @Override
    public void update() {
        counter++;
        if (counter % 10 == 0) {
            IBlockState left;
            IBlockState right;

            switch (facing) {
                case WEST:
                case EAST:
                    left = world.getBlockState(pos.add(0, 0, -1));
                    right = world.getBlockState(pos.add(0, 0, 1));
                    break;
                case NORTH:
                case SOUTH:
                default: // Will never happen but fuck you intelliJ errors
                    left = world.getBlockState(pos.add(-1, 0, 0));
                    right = world.getBlockState(pos.add(1, 0, 0));
                    break;
            }

            boolean lIsWater = left.getBlock() == Blocks.WATER;
            boolean rIsWater = right.getBlock() == Blocks.WATER;

            perTickRotationOwn = 0F;
            if (lIsWater) {
                perTickRotationOwn += ROTATION_PER_WHEEL;
            }

            if (rIsWater) {
                perTickRotationOwn -= ROTATION_PER_WHEEL;
            }

            float lastPerTickEffective = perTickEffective;
            perTickEffective = calcEffectivePerTickRotation(facing);

            if (lastPerTickEffective != perTickEffective) {
                markDirty();
            }
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
        return perTickRotationOwn;
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
