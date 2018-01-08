package exnihilocreatio.rotationalPower;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RotationalUtils {

    public static IRotationalPowerMember getPowerMember(World world, BlockPos pos, EnumFacing facing) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile != null && tile.hasCapability(CapabilityRotationalMember.ROTIONAL_MEMBER, facing)) {
            return tile.getCapability(CapabilityRotationalMember.ROTIONAL_MEMBER, facing);
        }
        return null;
    }

}
