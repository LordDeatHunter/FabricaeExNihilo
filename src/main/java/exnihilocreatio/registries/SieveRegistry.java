package exnihilocreatio.registries;

import com.google.common.collect.Lists;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.Siftable;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @deprecated use classes from [{@link exnihilocreatio.api.ExNihiloCreatioAPI}]
 */
@Deprecated
public class SieveRegistry {

    public static void register(BlockInfo block, ItemInfo drop, float chance, int meshLevel) {
        register(block, new Siftable(drop, chance, meshLevel));
    }

    public static void register(IBlockState state, ItemInfo drop, float chance, int meshLevel) {
        register(new BlockInfo(state), new Siftable(drop, chance, meshLevel));
    }

    public static void register(IBlockState state, ItemStack drop, float chance, int meshLevel) {
        register(new BlockInfo(state), new Siftable(new ItemInfo(drop), chance, meshLevel));
    }

    public static void register(BlockInfo block, Siftable drop) {
        ExNihiloRegistryManager.SIEVE_REGISTRY.register(CraftingHelper.getIngredient(block.getItemStack()), drop);
    }

    /**
     * Gets *all* possible drops from the sieve. It is up to the dropper to
     * check whether or not the drops should be dropped!
     *
     * @param block The block to get the sieve drops for
     * @return ArrayList of {@linkplain exnihilocreatio.registries.types.Siftable}
     * that could *potentially* be dropped.
     */
    public static ArrayList<Siftable> getDrops(BlockInfo block) {
        return (ArrayList<Siftable>) ExNihiloRegistryManager.SIEVE_REGISTRY.getDrops(block);
    }

    /**
     * Gets *all* possible drops from the sieve. It is up to the dropper to
     * check whether or not the drops should be dropped!
     *
     * @param block The block to get the sieve drops for
     * @return ArrayList of {@linkplain exnihilocreatio.registries.types.Siftable}
     * that could *potentially* be dropped.
     */
    public static ArrayList<Siftable> getDrops(ItemStack block) {
        return (ArrayList<Siftable>) ExNihiloRegistryManager.SIEVE_REGISTRY.getDrops(block);
    }

    public static List<ItemStack> getRewardDrops(Random random, IBlockState block, int meshLevel, int fortuneLevel) {
        if (block == null) {
            return null;
        }

        List<Siftable> siftables = getDrops(new BlockInfo(block));

        if (siftables == null) {
            return null;
        }

        List<ItemStack> drops = Lists.newArrayList();

        for (Siftable siftable : siftables) {
            if (meshLevel == siftable.getMeshLevel()) {
                int triesWithFortune = Math.max(random.nextInt(fortuneLevel + 2), 1);

                for (int i = 0; i < triesWithFortune; i++) {
                    if (random.nextDouble() < siftable.getChance()) {
                        drops.add(siftable.getDrop().getItemStack());
                    }
                }
            }
        }

        return drops;
    }

    public static boolean canBeSifted(ItemStack stack) {
        return ExNihiloRegistryManager.SIEVE_REGISTRY.canBeSifted(stack);
    }
}
