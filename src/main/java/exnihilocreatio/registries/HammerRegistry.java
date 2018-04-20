package exnihilocreatio.registries;

import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.HammerReward;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Deprecated
public class HammerRegistry {

    /**
     * Adds a new Hammer recipe for use with Ex Nihilo hammers.
     *
     * @param state         BlockStoneAxle State
     * @param reward        Reward
     * @param miningLevel   Mining level of hammer. 0 = Wood/Gold, 1 = Stone, 2 = Iron, 3 = Diamond. Can be higher, but will need corresponding tool material.
     * @param chance        Chance of drop
     * @param fortuneChance Chance of drop per level of fortune
     */
    public static void register(IBlockState state, ItemStack reward, int miningLevel, float chance, float fortuneChance) {
        register(state, reward, miningLevel, chance, fortuneChance, false);
    }

    public static void register(IBlockState state, ItemStack reward, int miningLevel, float chance, float fortuneChance, boolean wildcard) {
        ExNihiloRegistryManager.HAMMER_REGISTRY.register(state, reward, miningLevel, chance, fortuneChance);
    }

    public static List<ItemStack> getRewardDrops(Random random, IBlockState block, int miningLevel, int fortuneLevel) {
        return ExNihiloRegistryManager.HAMMER_REGISTRY.getRewardDrops(random, block, miningLevel, fortuneLevel);
    }

    @SuppressWarnings("unchecked")
    public static List<HammerReward> getRewards(IBlockState block) {
        return ExNihiloRegistryManager.HAMMER_REGISTRY.getRewards(block);
    }

    public static boolean registered(Block block) {
        return ExNihiloRegistryManager.HAMMER_REGISTRY.isRegistered(block);
    }

    // Legacy
    @Deprecated
    public static ArrayList<HammerReward> getRewards(IBlockState state, int miningLevel) {
        return (ArrayList<HammerReward>) ExNihiloRegistryManager.HAMMER_REGISTRY.getRewards(state, miningLevel);
    }
}
