package wraith.fabricaeexnihilo.compatibility.jade;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import wraith.fabricaeexnihilo.modules.strainer.StrainerBlockEntity;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class StrainerProvider implements IBlockComponentProvider {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!(blockAccessor.getBlockEntity() instanceof StrainerBlockEntity strainer)) return;
        var helper = tooltip.getElementHelper();
    
        var count = 0;
        for (ItemStack stack : strainer.getInventory()) {
            IElement item = helper.item(stack);
            if (count++ % 4 == 0) tooltip.add(item);
            else tooltip.append(item);
        }
    }
    
    @Override
    public Identifier getUid() {
        return id("strainer");
    }
}
