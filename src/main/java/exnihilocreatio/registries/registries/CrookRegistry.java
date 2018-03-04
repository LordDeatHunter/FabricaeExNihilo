package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.json.CustomIngredientJson;
import exnihilocreatio.json.CustomItemStackJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.CrookReward;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.IngredientUtil;
import exnihilocreatio.util.OreIngredientStoring;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrookRegistry extends BaseRegistryMap<Ingredient, NonNullList<CrookReward>> {

    public CrookRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemStack.class, new CustomItemStackJson())
                        .registerTypeAdapter(Ingredient.class, new CustomIngredientJson())
                        .registerTypeAdapter(OreIngredientStoring.class, new CustomIngredientJson())
                        .enableComplexMapKeySerialization()
                        .create(),
                new TypeToken<Map<Ingredient, List<CrookReward>>>() {
                }.getType(),
                ExNihiloRegistryManager.CROOK_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(Block block, int meta, ItemStack reward, float chance, float fortuneChance) {
        register(new BlockInfo(block, meta), reward, chance, fortuneChance);
    }

    public void register(IBlockState state, ItemStack reward, float chance, float fortuneChance) {
        register(new BlockInfo(state), reward, chance, fortuneChance);
    }

    public void register(BlockInfo info, ItemStack reward, float chance, float fortuneChance) {
        Ingredient ingredient = registry.keySet().stream().filter(entry -> entry.test(info.getItemStack())).findFirst().orElse(null);

        if (ingredient != null) {
            registry.get(ingredient).add(new CrookReward(reward, chance, fortuneChance));
        } else {
            NonNullList<CrookReward> list = NonNullList.create();
            list.add(new CrookReward(reward, chance, fortuneChance));
            registry.put(CraftingHelper.getIngredient(info), list);
        }
    }

    public void register(String name, ItemStack reward, float chance, float fortuneChance) {
        Ingredient ingredient = new OreIngredientStoring(name);
        if (ingredient.getMatchingStacks().length == 0)
            return;
        CrookReward crookReward = new CrookReward(reward, chance, fortuneChance);
        Ingredient search = registry.keySet().stream().filter(entry -> entry.getValidItemStacksPacked().equals(ingredient.getValidItemStacksPacked())).findAny().orElse(null);
        if (search != null) {
            registry.get(search).add(crookReward);
        } else {
            NonNullList<CrookReward> drops = NonNullList.create();
            drops.add(crookReward);
            registry.put(ingredient, drops);
        }
    }

    public boolean isRegistered(Block block) {
        ItemStack stack = new ItemStack(block);
        return registry.keySet().stream().anyMatch(ingredient -> ingredient.test(stack));
    }

    public List<CrookReward> getRewards(IBlockState state) {
        BlockInfo info = new BlockInfo(state);
        ArrayList<CrookReward> list = new ArrayList<>();

        registry.entrySet()
                .stream()
                .filter(ingredient -> ingredient.getKey().test(info.getItemStack()))
                .forEach(ingredient -> list.addAll(ingredient.getValue()));

        return list;
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        HashMap<String, NonNullList<CrookReward>> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, NonNullList<CrookReward>>>() {
        }.getType());

        gsonInput.forEach((key, value) -> {
            Ingredient ingredient = IngredientUtil.parseFromString(key);

            if (ingredient != null) {
                NonNullList<CrookReward> list = registry.getOrDefault(ingredient, NonNullList.create());
                list.addAll(value);
                registry.put(ingredient, list);
            }
        });
    }

    @Override
    public List<?> getRecipeList() {
        return Lists.newLinkedList();
    }
}
