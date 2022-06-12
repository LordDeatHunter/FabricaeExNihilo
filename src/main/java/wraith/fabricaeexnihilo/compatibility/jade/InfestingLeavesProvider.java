package wraith.fabricaeexnihilo.compatibility.jade;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import wraith.fabricaeexnihilo.modules.infested.InfestingLeavesBlockEntity;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class InfestingLeavesProvider implements IBlockComponentProvider {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!(blockAccessor.getBlockEntity() instanceof InfestingLeavesBlockEntity leaves)) return;
        tooltip.add(new TranslatableText("fabricaeexnihilo.hud.infesting_leaves.progress", (int) (leaves.getProgress() * 100)));
    }

    @Override
    public Identifier getUid() {
        return id("infesting_leaves");
    }
}
