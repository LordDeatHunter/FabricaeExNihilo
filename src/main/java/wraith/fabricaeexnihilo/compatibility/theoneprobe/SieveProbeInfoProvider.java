package wraith.fabricaeexnihilo.compatibility.theoneprobe;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlock;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlockEntity;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class SieveProbeInfoProvider implements IProbeInfoProvider {
    @Override
    public Identifier getID() {
        return id("sieve");
    }
    
    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        if (!(blockState.getBlock() instanceof SieveBlock) || !(world.getBlockEntity(data.getPos()) instanceof SieveBlockEntity sieve))
            return;
        
        if (!sieve.getMesh().isEmpty()) {
            probeInfo.text("fabricaeexnihilo.hud.sieve.mesh", sieve.getMesh().getName());
        }
        if (!sieve.getContents().isEmpty()) {
            probeInfo.text("fabricaeexnihilo.hud.sieve.content", sieve.getContents().getName());
            probeInfo.text("fabricaeexnihilo.hud.sieve.progress", (int) (sieve.getProgress() * 100));
        }
    }
}
