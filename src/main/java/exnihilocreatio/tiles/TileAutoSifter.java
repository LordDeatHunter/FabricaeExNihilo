package exnihilocreatio.tiles;

import exnihilocreatio.config.Config;
import exnihilocreatio.rotationalPower.IRotationalPowerConsumer;
import exnihilocreatio.rotationalPower.IRotationalPowerMember;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class TileAutoSifter extends BaseTileEntity implements ITickable, IRotationalPowerConsumer{
    public ArrayList<BlockPos> toSift = new ArrayList<>();

    public EnumFacing facing = EnumFacing.NORTH;
    public int tickCounter = 0;
    public float rotationValue = 0;
    public float perTickRotation = 0;

    public float offsetX = 0;
    public float offsetY = 0;
    public float offsetZ = 0;

    @Override
    public void update() {
        tickCounter++;

        if (world.isRemote && perTickRotation != 0){
            float r = 0.2F;
            float cx = 0F;
            float cz = 0F;

            offsetX = cx + r * (float)Math.cos(tickCounter / 4F);
            // offsetZ = cz + r * (float)Math.sin(tickCounter / 4F);
        }

        if (tickCounter % 10 == 0){
            perTickRotation = calcEffectivePerTickRotation(facing);
        }

        BlockPos posOther = pos.up();

        TileEntity te = world.getTileEntity(posOther);

        if (te != null && te instanceof TileSieve) {
            TileSieve sieve = (TileSieve) te;

            doAutoSieving(sieve);
        }
    }


    private void doAutoSieving(TileSieve thisSieve) {
        BlockPos sievePos = thisSieve.getPos();
        toSift.clear();


        for (int xOffset = -1 * Config.sieveSimilarRadius; xOffset <= Config.sieveSimilarRadius; xOffset++) {
            for (int zOffset = -1 * Config.sieveSimilarRadius; zOffset <= Config.sieveSimilarRadius; zOffset++) {
                TileEntity entity = world.getTileEntity(sievePos.add(xOffset, 0, zOffset));
                if (entity != null && entity instanceof TileSieve) {
                    TileSieve sieve = (TileSieve) entity;

                    if (thisSieve.isSieveSimilar(sieve))
                        toSift.add(sievePos.add(xOffset, 0, zOffset));
                }
            }
        }

        for (BlockPos posIter : toSift) {
            if (posIter != null) {
                TileSieve sieve = (TileSieve) world.getTileEntity(posIter);

                if (sieve != null) {
                    sieve.doSieving(null, true);
                    sieve.autoSifter = this;
                }
            }
        }
    }

    @Override
    public float getMachineRotationPerTick() {
        return perTickRotation;
    }

    @Override
    public void setEffectivePerTickRotation(float rotation) {
        perTickRotation = rotation;
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
}
