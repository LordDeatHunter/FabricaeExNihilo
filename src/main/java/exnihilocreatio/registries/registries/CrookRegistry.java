package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.api.registries.ICrookRegistry;
import exnihilocreatio.compatibility.jei.crook.CrookRecipe;
import exnihilocreatio.json.CustomIngredientJson;
import exnihilocreatio.json.CustomItemStackJson;
import exnihilocreatio.registries.ingredient.IngredientUtil;
import exnihilocreatio.registries.ingredient.OreIngredientStoring;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.CrookReward;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class CrookRegistry extends BaseRegistryMap<Ingredient, List<CrookReward>> implements ICrookRegistry {

    public CrookRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemStack.class, CustomItemStackJson.INSTANCE)
                        .registerTypeAdapter(Ingredient.class, CustomIngredientJson.INSTANCE)
                        .registerTypeAdapter(OreIngredientStoring.class, CustomIngredientJson.INSTANCE)
                        .enableComplexMapKeySerialization()
                        .create(),
                new TypeToken<Map<Ingredient, List<CrookReward>>>() {
                }.getType(),
                ExNihiloRegistryManager.CROOK_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(@NotNull Block block, int meta, @NotNull ItemStack reward, float chance, float fortuneChance) {
        register(new BlockInfo(block, meta), reward, chance, fortuneChance);
    }

    public void register(@NotNull IBlockState state, @NotNull ItemStack reward, float chance, float fortuneChance) {
        register(new BlockInfo(state), reward, chance, fortuneChance);
    }

    public void register(@NotNull BlockInfo info, @NotNull ItemStack reward, float chance, float fortuneChance) {
        Ingredient ingredient = registry.keySet().stream().filter(entry -> entry.test(info.getItemStack())).findFirst().orElse(null);

        if (ingredient != null) {
            registry.get(ingredient).add(new CrookReward(reward, chance, fortuneChance));
        } else {
            NonNullList<CrookReward> list = NonNullList.create();
            list.add(new CrookReward(reward, chance, fortuneChance));
            registry.put(CraftingHelper.getIngredient(info), list);
        }
    }

    public void register(@NotNull String name, @NotNull ItemStack reward, float chance, float fortuneChance) {
        Ingredient ingredient = new OreIngredientStoring(name);
        CrookReward crookReward = new CrookReward(reward, chance, fortuneChance);

        register(ingredient, crookReward);
    }

    public void register(@NotNull Ingredient ingredient, @NotNull CrookReward reward) {
        Ingredient search = registry.keySet()
                .stream()
                .filter(entry -> IngredientUtil.ingredientEquals(entry, ingredient))
                .findAny()
                .orElse(null);


        if (search != null) {
            registry.get(search).add(reward);
        } else {
            NonNullList<CrookReward> drops = NonNullList.create();
            drops.add(reward);
            registry.put(ingredient, drops);
        }
    }



    public NonNullList<CrookReward> getRewards(Ingredient ingredient) {
        NonNullList<CrookReward> drops = NonNullList.create();
        registry.entrySet().stream().filter(entry -> entry.getKey() == ingredient).forEach(entry -> drops.addAll(entry.getValue()));
        return drops;
    }

    public boolean isRegistered(@NotNull Block block) {
        ItemStack stack = new ItemStack(block);
        return registry.keySet().stream().anyMatch(ingredient -> ingredient.test(stack));
    }

    public boolean isRegistered(IBlockState state) {
        return isRegistered(new BlockInfo(state));
    }

    public boolean isRegistered(BlockInfo stackInfo) {
        return registry.keySet().stream().anyMatch(ingredient -> ingredient.test(stackInfo.getItemStack()));
    }

    @NotNull
    public List<CrookReward> getRewards(@NotNull IBlockState state) {
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
                List<CrookReward> list = registry.getOrDefault(ingredient, NonNullList.create());
                list.addAll(value);
                registry.put(ingredient, list);
            }
        });
    }

    @Override
    public List<CrookRecipe> getRecipeList() {
        List<CrookRecipe> recipes = Lists.newLinkedList();
        for(Ingredient ingredient : getRegistry().keySet()){
            if(ingredient == null)
                continue;
            List<ItemStack> rawOutputs = getRewards(ingredient).stream().map(CrookReward::getStack).collect(Collectors.toList());
            List<ItemStack> allOutputs = new ArrayList<>();
            for(ItemStack raw : rawOutputs){
                boolean alreadyexists = false;
                for(ItemStack all : allOutputs){
                    if(ItemUtil.areStacksEquivalent(all, raw)){
                        alreadyexists = true;
                        break;
                    }
                }
                if(!alreadyexists)
                    allOutputs.add(raw);
            }
            List<ItemStack> inputs = Arrays.asList(ingredient.getMatchingStacks());
            for(int i = 0; i < allOutputs.size(); i+=21){
                final List<ItemStack> outputs = allOutputs.subList(i, Math.min(i+21, allOutputs.size()));
                recipes.add(new CrookRecipe(inputs, outputs));
            }
        }
        return recipes;
    }
}
