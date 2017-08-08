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
    public float rotationValue = 0;
    public float perTickRotationOwn = 0F;
    public float perTickEffective = 0F;

    public EnumFacing facing = EnumFacing.NORTH;

    public boolean canTurn = true;

    private int counter = 0;
    private float lastPerTick = perTickRotationOwn;
    @Override
    public void update() {
        if (counter % 10 == 0){
            IBlockState left;
            IBlockState right;

            switch (facing){
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

            lastPerTick = perTickRotationOwn;
            perTickRotationOwn = 0F;
            if (lIsWater){
                perTickRotationOwn += 2F;
            }

            if (rIsWater){
                perTickRotationOwn += -2F;
            }

            if (lastPerTick != perTickRotationOwn){
                markDirty();
            }
        }


    }


    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {

        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
    }

    @Override
    public float getOwnRotation() {
        return perTickRotationOwn;
    }

    @Override
    public float getEffectivePerTickRotation() {
        BlockPos posProvider = pos.offset(facing.getOpposite());
        System.out.println("posProvider = " + posProvider);
        TileEntity te = world.getTileEntity(posProvider);
        if (te != null && te instanceof IRotationalPowerMember){
            return ((IRotationalPowerMember) te).getEffectivePerTickRotation() + getOwnRotation();
        }
        else return getOwnRotation();
    }

    @Override
    public void setEffectivePerTickRotation(float rotation) {
        perTickEffective = rotation;
    }
}
