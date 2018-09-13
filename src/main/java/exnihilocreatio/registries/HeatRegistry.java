package exnihilocreatio.registries;

import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.BlockInfo;
import net.minecraft.item.ItemStack;
/**
 * @deprecated use classes from [{@link exnihilocreatio.api.ExNihiloCreatioAPI}]
 */
@Deprecated
public class HeatRegistry {

    public static void register(BlockInfo info, int heatAmount) {
        ExNihiloRegistryManager.HEAT_REGISTRY.register(info, heatAmount);
    }

    public static int getHeatAmount(ItemStack stack) {
        return ExNihiloRegistryManager.HEAT_REGISTRY.getHeatAmount(stack);
    }

    public static int getHeatAmount(BlockInfo info) {
        return ExNihiloRegistryManager.HEAT_REGISTRY.getHeatAmount(info);
    }
}
