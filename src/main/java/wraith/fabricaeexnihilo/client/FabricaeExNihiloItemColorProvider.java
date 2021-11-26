package wraith.fabricaeexnihilo.client;

import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemStack;
import wraith.fabricaeexnihilo.modules.base.IHasColor;
import wraith.fabricaeexnihilo.util.Color;

public final class FabricaeExNihiloItemColorProvider implements ItemColorProvider {

    private FabricaeExNihiloItemColorProvider() {}

    public static final FabricaeExNihiloItemColorProvider INSTANCE = new FabricaeExNihiloItemColorProvider();

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        return stack.getItem() instanceof IHasColor colored ? colored.getColor(tintIndex) : Color.WHITE.toIntIgnoreAlpha();
    }

}