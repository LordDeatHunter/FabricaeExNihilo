package exnihilocreatio.tiles;

import exnihilocreatio.config.Config;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class TileAutoSiever extends BaseTileEntity implements ITickable {
    @Override
    public void update() {
        BlockPos posOther = pos.add(1, 0, 0);
        TileEntity te = world.getTileEntity(posOther);

        if (te != null && te instanceof TileSieve) {
            TileSieve sieve = (TileSieve) te;

            doAutoSieving(sieve);

            // sieve.doSieving(null, true);
        }
    }


    void doAutoSieving(TileSieve thisSieve) {
        ArrayList<BlockPos> toSift = new ArrayList<>();

        for (int xOffset = -1 * Config.sieveSimilarRadius; xOffset <= Config.sieveSimilarRadius; xOffset++) {
            for (int zOffset = -1 * Config.sieveSimilarRadius; zOffset <= Config.sieveSimilarRadius; zOffset++) {
                TileEntity entity = world.getTileEntity(pos.add(xOffset, 0, zOffset));
                if (entity != null && entity instanceof TileSieve) {
                    TileSieve sieve = (TileSieve) entity;

                    if (thisSieve.isSieveSimilar(sieve))
                        toSift.add(pos.add(xOffset, 0, zOffset));
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
