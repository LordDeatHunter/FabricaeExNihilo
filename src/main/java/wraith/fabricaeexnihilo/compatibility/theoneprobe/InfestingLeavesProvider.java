package wraith.fabricaeexnihilo.compatibility.theoneprobe;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.modules.infested.InfestingLeavesBlock;
import wraith.fabricaeexnihilo.modules.infested.InfestingLeavesBlockEntity;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class InfestingLeavesProvider implements IProbeInfoProvider {
    @Override
    public Identifier getID() {
        return id("infesting_leaves");
    }
    
    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        if (!(blockState.getBlock() instanceof InfestingLeavesBlock) || !(world.getBlockEntity(data.getPos()) instanceof InfestingLeavesBlockEntity leaves))
            return;
        
        probeInfo.text("fabricaeexnihilo.hud.infesting_leaves.progress", (int) (leaves.getProgress() * 100));
    }
}
