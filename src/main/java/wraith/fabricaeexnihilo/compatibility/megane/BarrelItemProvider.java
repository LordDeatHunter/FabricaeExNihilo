package wraith.fabricaeexnihilo.compatibility.megane;

import lol.bai.megane.api.provider.ItemProvider;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.modes.ItemMode;

class BarrelItemProvider extends ItemProvider<BarrelBlockEntity> {
    @Override
    public int getSlotCount() {
        if (!(getObject().getMode() instanceof ItemMode)) return 0;
        return 1;
    }
    
    @Override
    public @NotNull ItemStack getStack(int slot) {
        if (!(getObject().getMode() instanceof ItemMode mode)) return ItemStack.EMPTY;
        return mode.getStack();
    }
}
