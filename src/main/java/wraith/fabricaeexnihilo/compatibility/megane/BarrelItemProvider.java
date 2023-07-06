package wraith.fabricaeexnihilo.compatibility.megane;

import lol.bai.megane.api.provider.ItemProvider;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelState;

class BarrelItemProvider extends ItemProvider<BarrelBlockEntity> {
    @Override
    public int getSlotCount() {
        var barrel = getObject();
        if (barrel.getState() == BarrelState.ITEM) return 1;
        return 0;
    }

    @Override
    public @NotNull ItemStack getStack(int slot) {
        return getObject().getItem();
    }
}
