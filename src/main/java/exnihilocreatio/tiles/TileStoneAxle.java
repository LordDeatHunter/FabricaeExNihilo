package exnihilocreatio.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import javax.annotation.Nonnull;

public class TileStoneAxle extends BaseTileEntity implements ITickable {
    public float rotation = 0;
    public float perTick = 0F;

    private EnumFacing.Axis axis = EnumFacing.Axis.X;

    public boolean canTurn = true;

    int counter = 0;

    private float lastPerTick = perTick;
    @Override
    public void update() {
        if (counter % 10 == 0){
            IBlockState left;
            IBlockState right;
            switch (axis){
                case X:
                    left = world.getBlockState(pos.add(0, 0, -1));
                    right = world.getBlockState(pos.add(0, 0, 1));
                    break;
                case Z:
                default:
                    left = world.getBlockState(pos.add(-1, 0, 0));
                    right = world.getBlockState(pos.add(1, 0, 0));
                    break;
            }

            boolean lIsWater = left.getBlock() == Blocks.WATER;
            boolean rIsWater = right.getBlock() == Blocks.WATER;

            lastPerTick = perTick;
            perTick = 0F;
            if (lIsWater){
                perTick += 2F;
            }

            if (rIsWater){
                perTick += -2F;
            }

            if (lastPerTick != perTick){
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

    public EnumFacing.Axis getAxis() {
        return axis;
    }

    public void setAxis(EnumFacing.Axis axis) {
        if (axis == EnumFacing.Axis.Y){
            this.axis = EnumFacing.Axis.X;
        }else {
            this.axis = axis;
        }
    }
}
