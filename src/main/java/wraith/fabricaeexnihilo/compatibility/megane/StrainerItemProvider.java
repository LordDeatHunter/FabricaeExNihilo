package wraith.fabricaeexnihilo.compatibility.megane;

import lol.bai.megane.api.provider.ItemProvider;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import wraith.fabricaeexnihilo.modules.strainer.StrainerBlockEntity;

public class StrainerItemProvider extends ItemProvider<StrainerBlockEntity> {
    @Override
    public int getSlotCount() {
        return 8;
    }
    
    @Override
    public @NotNull ItemStack getStack(int slot) {
        return getObject().getInventory().get(slot);
    }
}
