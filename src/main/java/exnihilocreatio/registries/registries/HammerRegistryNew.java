package exnihilocreatio.registries.registries;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.json.CustomItemStackJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.HammerReward;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.io.FileReader;
import java.util.*;

public class HammerRegistryNew extends BaseRegistryMap<ItemInfo, List<HammerReward>> {

    public HammerRegistryNew() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemStack.class, new CustomItemStackJson())
                        .create(),
                ExNihiloRegistryManager.HAMMER_DEFAULT_REGISTRY_PROVIDERS
        );

        System.out.println("defaultRecipeProviders = " + defaultRecipeProviders);
        System.out.println("ExNihiloRegistryManager.HAMMER_DEFAULT_REGISTRY_PROVIDERS = " + ExNihiloRegistryManager.HAMMER_DEFAULT_REGISTRY_PROVIDERS);
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        HashMap<String, ArrayList<HammerReward>> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, ArrayList<HammerReward>>>() {
        }.getType());

        for (Map.Entry<String, ArrayList<HammerReward>> s : gsonInput.entrySet()) {
            ItemInfo stack = new ItemInfo(s.getKey());
            registry.put(stack, s.getValue());
        }
    }

    /**
     * Adds a new Hammer recipe for use with Ex Nihilo hammers.
     *
     * @param state         BlockStoneAxle State
     * @param reward        Reward
     * @param miningLevel   Mining level of hammer. 0 = Wood/Gold, 1 = Stone, 2 = Iron, 3 = Diamond. Can be higher, but will need corresponding tool material.
     * @param chance        Chance of drop
     * @param fortuneChance Chance of drop per level of fortune
     */
    public void register(IBlockState state, ItemStack reward, int miningLevel, float chance, float fortuneChance) {
        register(state, reward, miningLevel, chance, fortuneChance, false);
    }

    public void register(IBlockState state, ItemStack reward, int miningLevel, float chance, float fortuneChance, boolean wildcard) {
        ItemInfo key = new ItemInfo(state);
        if (wildcard)
            key.setMeta(-1);

        List<HammerReward> rewards = registry.get(key);

        if (rewards == null) {
            rewards = new ArrayList<>();
        }

        rewards.add(new HammerReward(reward, miningLevel, chance, fortuneChance));
        registry.put(key, rewards);
    }

    public List<ItemStack> getRewardDrops(Random random, IBlockState block, int miningLevel, int fortuneLevel) {
        List<ItemStack> rewards = new ArrayList<>();

        for (HammerReward reward : getRewards(block)) {
            if (miningLevel >= reward.getMiningLevel()) {
                if (random.nextFloat() <= reward.getChance() + (reward.getFortuneChance() * fortuneLevel)) {
                    rewards.add(reward.getStack().copy());
                }
            }
        }

        return rewards;
    }

    @SuppressWarnings("unchecked")
    public List<HammerReward> getRewards(IBlockState block) {
        return registry.getOrDefault(new ItemInfo(block), Collections.EMPTY_LIST);
    }

    public boolean registered(Block block) {
        return registry.containsKey(new ItemInfo(block.getDefaultState()));
    }

    // Legacy TODO: REMOVE if it works with ex compressum
    @Deprecated
    @SuppressWarnings("unchecked")
    public List<HammerReward> getRewards(IBlockState state, int miningLevel) {
        List<HammerReward> mapList = registry.getOrDefault(new ItemInfo(state), Collections.EMPTY_LIST);
        List<HammerReward> ret = new ArrayList<>();

        for (HammerReward reward : mapList) {
            if (reward.getMiningLevel() <= miningLevel)
                ret.add(reward);
        }

        return ret;
    }
}
