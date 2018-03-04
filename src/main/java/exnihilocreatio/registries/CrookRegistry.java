package exnihilocreatio.registries;

import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.CrookReward;
import exnihilocreatio.util.IStackInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

@Deprecated
public class CrookRegistry {
    public static void register(IStackInfo info, ItemStack reward, float chance, float fortuneChance) {
        ExNihiloRegistryManager.CROOK_REGISTRY.register(info, reward, chance, fortuneChance);
    }

    public static boolean registered(Block block) {
        return ExNihiloRegistryManager.CROOK_REGISTRY.isRegistered(block);
    }

    public static ArrayList<CrookReward> getRewards(IBlockState state) {
        return (ArrayList<CrookReward>) ExNihiloRegistryManager.CROOK_REGISTRY.getRewards(state);
    }
}
