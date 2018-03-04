package exnihilocreatio.registries;

import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.Meltable;
import exnihilocreatio.util.IStackInfo;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

@Deprecated
public class CrucibleRegistry {

    public static void register(IStackInfo item, Fluid fluid, int amount) {
        register(item, new Meltable(fluid.getName(), amount));
    }

    public static void register(ItemStack stack, Fluid fluid, int amount) {
        register(new ItemInfo(stack), new Meltable(fluid.getName(), amount));
    }

    public static void register(IStackInfo item, Meltable meltable) {
        ExNihiloRegistryManager.CRUCIBLE_STONE_REGISTRY.register(item, meltable);
    }


    public static boolean canBeMelted(ItemStack stack) {
        return ExNihiloRegistryManager.CRUCIBLE_STONE_REGISTRY.canBeMelted(stack);
    }

    public static boolean canBeMelted(IStackInfo info) {
        return ExNihiloRegistryManager.CRUCIBLE_STONE_REGISTRY.canBeMelted(info);
    }

    public static Meltable getMeltable(ItemStack stack) {
        return ExNihiloRegistryManager.CRUCIBLE_STONE_REGISTRY.getMeltable(stack);
    }

    public static Meltable getMeltable(IStackInfo info) {
        return ExNihiloRegistryManager.CRUCIBLE_STONE_REGISTRY.getMeltable(info);
    }
}
