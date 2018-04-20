package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.compatibility.jei.hammer.HammerRecipe;
import exnihilocreatio.json.CustomHammerRewardJson;
import exnihilocreatio.json.CustomIngredientJson;
import exnihilocreatio.json.CustomItemStackJson;
import exnihilocreatio.registries.ingredient.IngredientUtil;
import exnihilocreatio.registries.ingredient.OreIngredientStoring;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.HammerReward;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.StackInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.io.FileReader;
import java.util.*;

public class HammerRegistry extends BaseRegistryMap<Ingredient, NonNullList<HammerReward>> {

    public HammerRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemStack.class, new CustomItemStackJson())
                        .registerTypeAdapter(Ingredient.class, new CustomIngredientJson())
                        .registerTypeAdapter(OreIngredientStoring.class, new CustomIngredientJson())
                        .registerTypeAdapter(HammerReward.class, new CustomHammerRewardJson())
                        .enableComplexMapKeySerialization()
                        .create(),
                new com.google.gson.reflect.TypeToken<Map<Ingredient, List<HammerReward>>>() {}.getType(),
                ExNihiloRegistryManager.HAMMER_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        HashMap<String, ArrayList<HammerReward>> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, ArrayList<HammerReward>>>() {
        }.getType());

        for (Map.Entry<String, ArrayList<HammerReward>> s : gsonInput.entrySet()) {
            Ingredient ingredient = IngredientUtil.parseFromString(s.getKey());


            Ingredient search = registry.keySet().stream().filter(entry -> IngredientUtil.ingredientEquals(ingredient, entry)).findAny().orElse(null);
            if (search != null) {
                registry.get(search).addAll(s.getValue());
            } else {
                NonNullList<HammerReward> drops = NonNullList.create();
                drops.addAll(s.getValue());
                registry.put(ingredient, drops);
            }
        }
    }

    /**
     * Adds a new Hammer recipe for use with Ex Nihilo hammers.
     *
     * @param state         The blocks state to add
     * @param reward        Reward
     * @param miningLevel   Mining level of hammer. 0 = Wood/Gold, 1 = Stone, 2 = Iron, 3 = Diamond. Can be higher, but will need corresponding tool material.
     * @param chance        Chance of drop
     * @param fortuneChance Chance of drop per level of fortune
     */
    public void register(IBlockState state, ItemStack reward, int miningLevel, float chance, float fortuneChance) {
        register(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)), new HammerReward(reward, miningLevel, chance, fortuneChance));
    }

    public void register(Block block, int meta, ItemStack reward, int miningLevel, float chance, float fortuneChance) {
        register(new ItemStack(block, 1, meta), new HammerReward(reward, miningLevel, chance, fortuneChance));
    }

    public void register(StackInfo stackInfo, ItemStack reward, int miningLevel, float chance, float fortuneChance) {
        register(stackInfo.getItemStack(), new HammerReward(reward, miningLevel, chance, fortuneChance));
    }

    public void register(ItemStack stack, HammerReward reward) {
        if (stack.isEmpty())
            return;
        Ingredient ingredient = CraftingHelper.getIngredient(stack);
        register(ingredient, reward);
    }

    public void register(String name, ItemStack reward, int miningLevel, float chance, float fortuneChance) {
        Ingredient ingredient = new OreIngredientStoring(name);
        register(ingredient, new HammerReward(reward, miningLevel, chance, fortuneChance));
    }

    public void register(Ingredient ingredient, HammerReward reward) {
        Ingredient search = registry.keySet().stream().filter(entry -> IngredientUtil.ingredientEquals(ingredient, entry)).findAny().orElse(null);

        if (search != null) {
            registry.get(search).add(reward);
        } else {
            NonNullList<HammerReward> drops = NonNullList.create();
            drops.add(reward);
            registry.put(ingredient, drops);
        }
    }

    public NonNullList<ItemStack> getRewardDrops(Random random, IBlockState block, int miningLevel, int fortuneLevel) {
        NonNullList<ItemStack> rewards = NonNullList.create();

        for (HammerReward reward : getRewards(block)) {
            if (miningLevel >= reward.getMiningLevel()) {
                if (random.nextFloat() <= reward.getChance() + (reward.getFortuneChance() * fortuneLevel)) {
                    rewards.add(reward.getStack().copy());
                }
            }
        }

        return rewards;
    }

    public NonNullList<HammerReward> getRewards(IBlockState block) {
        return getRewards(new BlockInfo(block));
    }

    public NonNullList<HammerReward> getRewards(Block block, int meta) {
        return getRewards(new BlockInfo(block, meta));
    }

    public NonNullList<HammerReward> getRewards(BlockInfo stackInfo) {
        NonNullList<HammerReward> drops = NonNullList.create();
        if (stackInfo.isValid())
            registry.entrySet().stream().filter(entry -> entry.getKey().test(stackInfo.getItemStack())).forEach(entry -> drops.addAll(entry.getValue()));
        return drops;
    }

    public NonNullList<HammerReward> getRewards(Ingredient ingredient) {
        NonNullList<HammerReward> drops = NonNullList.create();
        registry.entrySet().stream().filter(entry -> entry.getKey() == ingredient).forEach(entry -> drops.addAll(entry.getValue()));
        return drops;
    }

    public boolean isRegistered(IBlockState block) {
        return isRegistered(new BlockInfo(block));
    }

    public boolean isRegistered(Block block) {
        return isRegistered(new BlockInfo(block.getDefaultState()));
    }

    public boolean isRegistered(BlockInfo stackInfo) {
        return registry.keySet().stream().anyMatch(ingredient -> ingredient.test(stackInfo.getItemStack()));
    }

    // Legacy TODO: REMOVE if it works with ex compressum
    @Deprecated
    public List<HammerReward> getRewards(IBlockState state, int miningLevel) {
        NonNullList<HammerReward> drops = NonNullList.create();
        BlockInfo block = new BlockInfo(state);

        registry.entrySet().stream().filter(entry -> entry.getKey().test(block.getItemStack()))
                .forEach(entry -> entry.getValue().stream().filter(value -> value.getMiningLevel() <= miningLevel).forEach(drops::add));

        return drops;
    }

    @Override
    public List<HammerRecipe> getRecipeList() {
        List<HammerRecipe> hammerRecipes = Lists.newLinkedList();
        getRegistry().keySet().forEach(ingredient -> {
            HammerRecipe recipe = new HammerRecipe(ingredient);
            if (recipe.isValid())
                hammerRecipes.add(recipe);
        });
        return hammerRecipes;
    }
}
