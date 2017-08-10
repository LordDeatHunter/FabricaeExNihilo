package exnihilocreatio.tiles;

import exnihilocreatio.config.Config;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class TileAutoSifter extends BaseTileEntity implements ITickable{
    public ArrayList<BlockPos> toSift = new ArrayList<>();


    @Override
    public void update() {
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
                }
            }
        }
    }
}
