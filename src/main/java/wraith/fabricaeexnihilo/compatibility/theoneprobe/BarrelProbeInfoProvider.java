package wraith.fabricaeexnihilo.compatibility.theoneprobe;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlock;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.modes.AlchemyMode;
import wraith.fabricaeexnihilo.modules.barrels.modes.CompostMode;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

class BarrelProbeInfoProvider implements IProbeInfoProvider {
    @Override
    public Identifier getID() {
        return id("barrel");
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        if (!(blockState.getBlock() instanceof BarrelBlock) || !(world.getBlockEntity(data.getPos()) instanceof BarrelBlockEntity barrel))
            return;

        var barrelMode = barrel.getMode();
        if (barrelMode instanceof CompostMode compostMode) {
            if (compostMode.getAmount() < 1) {
                probeInfo.text(Text.translatable("fabricaeexnihilo.hud.barrel.compost.filling", (int) (compostMode.getAmount() * 100)));
            } else {
                probeInfo.text(Text.translatable("fabricaeexnihilo.hud.barrel.compost.composting", (int) (compostMode.getProgress() * 100)));
            }
        } else if (barrelMode instanceof AlchemyMode alchemyMode) {
            probeInfo.text(Text.translatable("fabricaeexnihilo.hud.barrel.alchemy.processing", (int) ((100.0 / alchemyMode.getDuration()) * alchemyMode.getProgress())));
        }
    }
}
