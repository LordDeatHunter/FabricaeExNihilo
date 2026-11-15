package wraith.fabricaeexnihilo.compatibility.jade;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlockEntity;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class CrucibleProvider implements IBlockComponentProvider {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!(blockAccessor.getBlockEntity() instanceof CrucibleBlockEntity crucible)) return;

        // Which solid is inside. `queued` only reports what is left to melt, so on its own
        // it goes quiet the moment the last piece finishes - the crucible then looks empty
        // even though the fluid it produced is still in there. Report the solid for as long
        // as there is anything to show it for, and the remaining melt separately.
        var stack = crucible.getDisplayStack();
        if (!stack.isEmpty()) {
            tooltip.add(Text.translatable("fabricaeexnihilo.hud.crucible.contents",
                    stack.getName(), stack.getCount()));
        }
        if (crucible.getQueued() > 0) {
            tooltip.add(Text.translatable("fabricaeexnihilo.hud.crucible.queued", crucible.getQueued() / 81));
        }
        if (crucible.getContained() > 0) {
            var fluid = crucible.getFluid();
            tooltip.add(Text.translatable("fabricaeexnihilo.hud.fluid_content", FluidVariantAttributes.getName(fluid), crucible.getContained() / 81, crucible.getMaxCapacity() / 81));
        }
    }

    @Override
    public Identifier getUid() {
        return id("crucible");
    }
}
