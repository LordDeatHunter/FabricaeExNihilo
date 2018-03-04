package exnihilocreatio.registries;

import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.IStackInfo;
import net.minecraft.item.ItemStack;

@Deprecated
public class HeatRegistry {

    public static void register(IStackInfo info, int heatAmount) {
        ExNihiloRegistryManager.HEAT_REGISTRY.register(info, heatAmount);
    }

    public static int getHeatAmount(ItemStack stack) {
        return ExNihiloRegistryManager.HEAT_REGISTRY.getHeatAmount(stack);
    }

    public static int getHeatAmount(IStackInfo info) {
        return ExNihiloRegistryManager.HEAT_REGISTRY.getHeatAmount(info);
    }
}
