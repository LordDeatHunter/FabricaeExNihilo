package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.blocks.BlockSieve;
import exnihilocreatio.compatibility.jei.sieve.SieveRecipe;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.Siftable;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.FileReader;
import java.util.*;

public class SieveRegistry extends BaseRegistryMap<Ingredient, NonNullList<Siftable>> {

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


    public void register(ItemStack itemStack, ItemInfo drop, float chance, int meshLevel) {
        if (itemStack.isEmpty()) {
            return;
        }
        register(CraftingHelper.getIngredient(itemStack), new Siftable(drop, chance, meshLevel));
    }

    public void register(Item item, int meta, ItemInfo drop, float chance, int meshLevel) {
        register(new ItemStack(item, 1, meta), drop, chance, meshLevel);
    }

    public void register(ItemInfo item, ItemInfo drop, float chance, int meshLevel) {
        register(item.getItemStack(), drop, chance, meshLevel);
    }

    public void register(Block block, int meta, ItemInfo drop, float chance, int meshLevel) {
        register(new ItemStack(block, 1, meta), drop, chance, meshLevel);
    }

    public void register(BlockInfo info, ItemInfo drop, float chance, int meshLevel) {
        register(info.getItemStack(), drop, chance, meshLevel);
    }

    public void register(IBlockState state, ItemInfo drop, float chance, int meshLevel) {
        register(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)), drop, chance, meshLevel);
    }

    public void register(ResourceLocation location, int meta, ItemInfo drop, float chance, int meshLevel) {
        register(new ItemStack(ForgeRegistries.ITEMS.getValue(location), 1, meta), drop, chance, meshLevel);
    }

    public void register(String name, ItemInfo drop, float chance, int meshLevel) {
        register(CraftingHelper.getIngredient(name), new Siftable(drop, chance, meshLevel));
    }

    public void register(Ingredient ingredient, Siftable drop) {
        if (ingredient == null || ingredient.getMatchingStacks().length == 0) {
            return;
        }

        Ingredient search = registry.keySet().stream().filter(entry -> entry.getValidItemStacksPacked().equals(ingredient.getValidItemStacksPacked())).findAny().orElse(null);
        if (search != null){
            registry.get(search).add(drop);
        }
        else {
            NonNullList<Siftable> drops = NonNullList.create();
            drops.add(drop);
            registry.put(ingredient, drops);
        }
    }

    /**
     * Gets *all* possible drops from the sieve. It is up to the dropper to
     * check whether or not the drops should be dropped!
     *
     * @param block The block to get the sieve drops for
     * @return ArrayList of {@linkplain Siftable}
     * that could *potentially* be dropped.
     */
    public List<Siftable> getDrops(BlockInfo block) {
        return getDrops(block.getItemStack());
    }

    /**
     * Gets *all* possible drops from the sieve. It is up to the dropper to
     * check whether or not the drops should be dropped!
     *
     * @param stack The ItemStack to get the sieve drops for
     * @return ArrayList of {@linkplain Siftable}
     * that could *potentially* be dropped.
     */
    public List<Siftable> getDrops(ItemStack stack) {
        List<Siftable> drops = new ArrayList<>();
        if (!stack.isEmpty())
            registry.entrySet().stream().filter(entry -> entry.getKey().test(stack)).forEach(entry -> drops.addAll(entry.getValue()));
        return drops;
    }

    public List<Siftable> getDrops(Ingredient ingredient){
        List<Siftable> drops = new ArrayList<>();
        registry.entrySet().stream().filter(entry -> entry.getKey() == ingredient).forEach(entry -> drops.addAll(entry.getValue()));
        return drops;
    }

    public List<ItemStack> getRewardDrops(Random random, IBlockState block, int meshLevel, int fortuneLevel) {
        if (block == null) {
            return null;
        }

        List<ItemStack> drops = new ArrayList<>();

        getDrops(new BlockInfo(block)).forEach(siftable -> {
            if (meshLevel == siftable.getMeshLevel()) {
                int triesWithFortune = Math.max(random.nextInt(fortuneLevel + 2), 1);

                for (int i = 0; i < triesWithFortune; i++) {
                    if (random.nextDouble() < siftable.getChance()) {
                        drops.add(siftable.getDrop().getItemStack());
                    }
                }
            }
        });

        return drops;
    }

    public boolean canBeSifted(ItemStack stack) {
        return !stack.isEmpty() && registry.keySet().stream().anyMatch(entry -> entry.test(stack));
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        HashMap<String, ArrayList<Siftable>> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, ArrayList<Siftable>>>() {
        }.getType());

        for (Map.Entry<String, ArrayList<Siftable>> input : gsonInput.entrySet()) {
            Ingredient block = CraftingHelper.getIngredient(input.getKey());

            if (block != null) {
                for (Siftable siftable : input.getValue()) {
                    if (siftable.getDrop().isValid()) {
                        register(block, siftable);
                    }
                }
            }
        }
    }

    @Override
    public List<SieveRecipe> getRecipeList() {
        List<SieveRecipe> sieveRecipes = Lists.newArrayList();

        for (Ingredient ingredient : getRegistry().keySet()) {
            for (BlockSieve.MeshType type : BlockSieve.MeshType.values()) {
                if (type.getID() != 0 && ingredient != null) // Bad configs strike back!
                {
                    SieveRecipe recipe = new SieveRecipe(ingredient, type);

                    // If there's an input block, mesh, and at least one output
                    if (!recipe.getInputs().isEmpty() && !recipe.getOutputs().isEmpty() && !sieveRecipes.contains(recipe)) {

                        sieveRecipes.add(recipe);
                    }
                }
            }
        }

        return sieveRecipes;
    }
}
