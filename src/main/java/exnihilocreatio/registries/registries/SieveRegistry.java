package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.Siftable;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.io.FileReader;
import java.util.*;

public class SieveRegistry extends BaseRegistryMap<BlockInfo, ArrayList<Siftable>> {

    public SieveRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson())
                        .registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson())
                        .create(),
                ExNihiloRegistryManager.SIEVE_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(BlockInfo block, ItemInfo drop, float chance, int meshLevel) {
        register(block, new Siftable(drop, chance, meshLevel));
    }

    public void register(IBlockState state, ItemInfo drop, float chance, int meshLevel) {
        register(new BlockInfo(state), new Siftable(drop, chance, meshLevel));
    }

    public void register(IBlockState state, ItemStack drop, float chance, int meshLevel) {
        register(new BlockInfo(state), new Siftable(new ItemInfo(drop), chance, meshLevel));
    }

    public void register(BlockInfo block, Siftable drop) {
        if (block == null) {
            return;
        }

        ArrayList<Siftable> drops = registry.get(block);

        if (drops == null) {
            drops = new ArrayList<>();
        }

        drops.add(drop);
        registry.put(block, drops);
    }

    /**
     * Gets *all* possible drops from the sieve. It is up to the dropper to
     * check whether or not the drops should be dropped!
     *
     * @param block
     * @return ArrayList of {@linkplain Siftable}
     * that could *potentially* be dropped.
     */
    public List<Siftable> getDrops(BlockInfo block) {
        if (!registry.containsKey(block))
            return null;

        return registry.get(block);
    }

    /**
     * Gets *all* possible drops from the sieve. It is up to the dropper to
     * check whether or not the drops should be dropped!
     *
     * @param block
     * @return ArrayList of {@linkplain Siftable}
     * that could *potentially* be dropped.
     */
    public List<Siftable> getDrops(ItemStack block) {
        return getDrops(new BlockInfo(block));
    }

    public List<ItemStack> getRewardDrops(Random random, IBlockState block, int meshLevel, int fortuneLevel) {
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

    public boolean canBeSifted(ItemStack stack) {
        return stack != null && registry.containsKey(new BlockInfo(stack));
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        HashMap<String, ArrayList<Siftable>> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, ArrayList<Siftable>>>() {
        }.getType());

        for (Map.Entry<String, ArrayList<Siftable>> input : gsonInput.entrySet()) {
            BlockInfo block = new BlockInfo(input.getKey());

            if (block.getBlock() != null) {
                for (Siftable siftable : input.getValue()) {
                    if (siftable.getDrop().isValid()) {
                        register(block, siftable);
                    }
                }
            }
        }
    }

}
