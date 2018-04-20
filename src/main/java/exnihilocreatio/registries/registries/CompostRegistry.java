package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.compatibility.jei.barrel.compost.CompostRecipe;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomColorJson;
import exnihilocreatio.json.CustomCompostableJson;
import exnihilocreatio.json.CustomIngredientJson;
import exnihilocreatio.registries.ingredient.IngredientUtil;
import exnihilocreatio.registries.ingredient.OreIngredientStoring;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.Compostable;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.LogUtil;
import exnihilocreatio.util.StackInfo;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompostRegistry extends BaseRegistryMap<Ingredient, Compostable> {

    protected final Map<Ingredient, Compostable> oreRegistry = new HashMap<>();

    public CompostRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(Ingredient.class, new CustomIngredientJson())
                        .registerTypeAdapter(OreIngredientStoring.class, new CustomIngredientJson())
                        .registerTypeAdapter(Compostable.class, new CustomCompostableJson())
                        .registerTypeAdapter(Color.class, new CustomColorJson())
                        .registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson())
                        .enableComplexMapKeySerialization()
                        .create(),
                new TypeToken<Map<Ingredient, Compostable>>() {
                }.getType(),
                ExNihiloRegistryManager.COMPOST_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(ItemStack itemStack, float value, BlockInfo state, Color color) {
        if (itemStack.isEmpty())
            return;

        Ingredient ingredient = CraftingHelper.getIngredient(itemStack);

        if (registry.keySet().stream().anyMatch(entry -> entry.test(itemStack))) {
            LogUtil.error("Compost Entry for " + itemStack.getItem().getRegistryName() + " with meta " + itemStack.getMetadata() + " already exists, skipping.");
            return;
        }
        Compostable compostable = new Compostable(value, color, state);
        register(ingredient, compostable);
    }

    public void register(Item item, int meta, float value, BlockInfo state, Color color) {
        register(new ItemStack(item, 1, meta), value, state, color);
    }

    public void register(Block block, int meta, float value, BlockInfo state, Color color) {
        register(new ItemStack(block, 1, meta), value, state, color);
    }

    public void register(StackInfo item, float value, BlockInfo state, Color color) {
        register(item.getItemStack(), value, state, color);
    }

    public void register(ResourceLocation location, int meta, float value, BlockInfo state, Color color) {
        register(ForgeRegistries.ITEMS.getValue(location), meta, value, state, color);
    }

    public void register(String name, float value, BlockInfo state, Color color) {
        Ingredient ingredient = new OreIngredientStoring(name);
        Compostable compostable = new Compostable(value, color, state);

        if (oreRegistry.keySet().stream().anyMatch(entry -> IngredientUtil.ingredientEquals(entry, ingredient)))
            LogUtil.error("Compost Ore Entry for " + name + " already exists, skipping.");
        else
            register(ingredient, compostable);
    }

    /**
     * Registers a oredict for sifting with a dynamic color based on the itemColor
     */
    public void register(String name, float value, BlockInfo state) {
        register(name, value, state, Color.INVALID_COLOR);
    }

    public Compostable getItem(Item item, int meta) {
        return getItem(new ItemStack(item, meta));
    }

    public Compostable getItem(ItemStack stack) {
        Ingredient ingredient = registry.keySet().stream().filter(entry -> entry.test(stack)).findFirst().orElse(null);
        if (ingredient != null) return registry.get(ingredient);
        ingredient = oreRegistry.keySet().stream().filter(entry -> entry.test(stack)).findFirst().orElse(null);
        if (ingredient != null) return oreRegistry.get(ingredient);
        else return Compostable.EMPTY;
    }

    public Compostable getItem(StackInfo info) {
        return getItem(info.getItemStack());
    }

    public boolean containsItem(Item item, int meta) {
        return containsItem(new ItemStack(item, meta));
    }

    public boolean containsItem(ItemStack stack) {
        return registry.keySet().stream().anyMatch(entry -> entry.test(stack)) || oreRegistry.keySet().stream().anyMatch(entry -> entry.test(stack));
    }

    public boolean containsItem(StackInfo info) {
        return containsItem(info.getItemStack());
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        Map<String, Compostable> gsonInput = gson.fromJson(fr, new TypeToken<Map<String, Compostable>>() {
        }.getType());

        for (Map.Entry<String, Compostable> entry : gsonInput.entrySet()) {
            Ingredient ingr = IngredientUtil.parseFromString(entry.getKey());

            if (registry.keySet().stream().anyMatch(ingredient -> IngredientUtil.ingredientEquals(ingredient, ingr)))
                LogUtil.error("Compost JSON Entry for " + entry.getKey() + " already exists, skipping.");
            else
                register(ingr, entry.getValue());
        }
    }

    @Override
    public Map<Ingredient, Compostable> getRegistry() {
        //noinspection unchecked
        Map<Ingredient, Compostable> map = (HashMap) ((HashMap) registry).clone();
        map.putAll(oreRegistry);
        return map;
    }

    @Override
    public List<CompostRecipe> getRecipeList() {
        List<CompostRecipe> compostRecipePages = new ArrayList<>();

        getRegistry().forEach((key, value) -> {
            BlockInfo compostBlock = value.getCompostBlock();
            List<ItemStack> compostables = Lists.newLinkedList();
            int compostCount = (int) Math.ceil(1.0F / value.getValue());

            ItemStack[] stacks = key.getMatchingStacks();
            if (stacks.length <= 0) return;

            for (ItemStack stack : stacks) {
                if (compostables.stream().noneMatch(stack::isItemEqual)) {
                    ItemStack copy = stack.copy();
                    copy.setCount(compostCount);
                    compostables.add(copy);
                }
            }

            CompostRecipe recipe = compostRecipePages.stream()
                    .filter(compostRecipe -> compostRecipe.outputMatch(compostBlock.getItemStack())
                            && compostRecipe.isNonFull())
                    .findFirst()
                    .orElse(null);

            if (recipe == null) {
                recipe = new CompostRecipe(compostBlock, new ArrayList<>());
                compostRecipePages.add(recipe);
            }

            //This acts as a safety net, auto creating new recipes if the input list is larger than 45
            if (recipe.isNonFull()) {
                recipe.getInputs().add(compostables);
            } else {
                recipe = new CompostRecipe(compostBlock, Lists.newLinkedList());
                recipe.getInputs().add(compostables);
                compostRecipePages.add(recipe);
            }
        });

        return compostRecipePages;
    }
}
