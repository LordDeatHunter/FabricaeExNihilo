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
import wraith.fabricaeexnihilo.modules.barrels.BarrelState;

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

        if (barrel.isCrafting()) {
            probeInfo.text(Text.translatable("fabricaeexnihilo.hud.barrel.alchemy.processing", (int) (100.0 * barrel.getRecipeProgress())));
            return;
        }

        if (barrel.getState() == BarrelState.COMPOST) {
            if (barrel.getCompostLevel() < 1) {
                probeInfo.text(Text.translatable("fabricaeexnihilo.hud.barrel.compost.filling", (int) (barrel.getCompostLevel() * 100)));
            } else {
                probeInfo.text(Text.translatable("fabricaeexnihilo.hud.barrel.compost.composting", (int) (barrel.getRecipeProgress() * 100)));
            }
        }
    }
}
