package wraith.fabricaeexnihilo.compatibility.theoneprobe;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlock;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlock;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlockEntity;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class CrucibleProbeInfoProvider implements IProbeInfoProvider {
    @Override
    public Identifier getID() {
        return id("crucible");
    }
    
    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        if (!(blockState.getBlock() instanceof CrucibleBlock) || !(world.getBlockEntity(data.getPos()) instanceof CrucibleBlockEntity crucible))
            return;
    
        if (crucible.getQueued() > 0 && mode == ProbeMode.EXTENDED) {
            probeInfo.text(new TranslatableText("fabricaeexnihilo.hud.crucible.queued", crucible.getQueued() / 81 /* Convert to millibuckets */));
        }
    }
}
