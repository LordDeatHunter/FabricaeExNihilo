package wraith.fabricaeexnihilo.compatibility.jade;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlockEntity;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class SieveProvider implements IBlockComponentProvider {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!(blockAccessor.getBlockEntity() instanceof SieveBlockEntity sieve)) return;
    
        if (!sieve.getMesh().isEmpty()) {
            tooltip.add(Text.translatable("fabricaeexnihilo.hud.sieve.mesh", sieve.getMesh().getName()));
        }
        if (!sieve.getContents().isEmpty()) {
            tooltip.add(Text.translatable("fabricaeexnihilo.hud.sieve.content", sieve.getContents().getName()));
            tooltip.add(Text.translatable("fabricaeexnihilo.hud.sieve.progress", (int) (sieve.getProgress() * 100)));
        }
    }
    
    @Override
    public Identifier getUid() {
        return id("sieve");
    }
}
