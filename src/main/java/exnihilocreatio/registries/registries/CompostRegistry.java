package exnihilocreatio.registries.registries;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.api.registries.ICompostRegistry;
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
import exnihilocreatio.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompostRegistry extends BaseRegistryMap<Ingredient, Compostable> implements ICompostRegistry {

    protected final Map<Ingredient, Compostable> oreRegistry = new HashMap<>();

    public CompostRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(Ingredient.class, CustomIngredientJson.INSTANCE)
                        .registerTypeAdapter(OreIngredientStoring.class, CustomIngredientJson.INSTANCE)
                        .registerTypeAdapter(Compostable.class, CustomCompostableJson.INSTANCE)
                        .registerTypeAdapter(Color.class, CustomColorJson.INSTANCE)
                        .registerTypeAdapter(BlockInfo.class, CustomBlockInfoJson.INSTANCE)
                        .enableComplexMapKeySerialization()
                        .create(),
                new TypeToken<Map<Ingredient, Compostable>>() {
                }.getType(),
                ExNihiloRegistryManager.COMPOST_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(@NotNull ItemStack itemStack, float value, @NotNull BlockInfo state, @NotNull Color color) {
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

    public void register(Item item, int meta, float value, @NotNull BlockInfo state, @NotNull Color color) {
        register(new ItemStack(item, 1, meta), value, state, color);
    }

    public void register(@NotNull Block block, int meta, float value, @NotNull BlockInfo state, @NotNull Color color) {
        register(new ItemStack(block, 1, meta), value, state, color);
    }

    public void register(@NotNull StackInfo item, float value, @NotNull BlockInfo state, @NotNull Color color) {
        register(item.getItemStack(), value, state, color);
    }

    public void register(@NotNull ResourceLocation location, int meta, float value, @NotNull BlockInfo state, @NotNull Color color) {
        register(ForgeRegistries.ITEMS.getValue(location), meta, value, state, color);
    }

    public void register(@NotNull String name, float value, @NotNull BlockInfo state, @NotNull Color color) {
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
    public void register(@NotNull String name, float value, @NotNull BlockInfo state) {
        register(name, value, state, Color.INVALID_COLOR);
    }

    @NotNull
    public Compostable getItem(@NotNull Item item, int meta) {
        return getItem(new ItemStack(item, meta));
    }

    @NotNull
    public Compostable getItem(@NotNull ItemStack stack) {
        Ingredient ingredient = registry.keySet().stream().filter(entry -> entry.test(stack)).findFirst().orElse(null);
        if (ingredient != null) return registry.get(ingredient);
        ingredient = oreRegistry.keySet().stream().filter(entry -> entry.test(stack)).findFirst().orElse(null);
        if (ingredient != null) return oreRegistry.get(ingredient);
        else return Compostable.Companion.getEMPTY();
    }

    @NotNull
    public Compostable getItem(@NotNull StackInfo info) {
        return getItem(info.getItemStack());
    }

    public boolean containsItem(@NotNull Item item, int meta) {
        return containsItem(new ItemStack(item, meta));
    }

    public boolean containsItem(@NotNull ItemStack stack) {
        return registry.keySet().stream().anyMatch(entry -> entry.test(stack)) || oreRegistry.keySet().stream().anyMatch(entry -> entry.test(stack));
    }

    public boolean containsItem(@NotNull StackInfo info) {
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
        List<CompostRecipe> recipes = new ArrayList<>();

        Map<BlockInfo, List<List<ItemStack>>> outputMap = new HashMap<>();
        for(Map.Entry<Ingredient, Compostable> entry : getRegistry().entrySet()){
            BlockInfo output = entry.getValue().getCompostBlock();
            Ingredient ingredient = entry.getKey();
            if(ingredient == null || !output.isValid())
                continue;
            // Initialize new outputs
            if(!outputMap.containsKey(output)){
                List<List<ItemStack>> inputs = new ArrayList<>();
                outputMap.put(output, inputs);
            }
            // Collect all the potential itemstacks which match this ingredient
            List<ItemStack> inputs = new ArrayList<>();
            for(ItemStack match : ingredient.getMatchingStacks()){
                if(match.isEmpty())
                    continue;
                ItemStack input = match.copy();
//                input.setCount((int) Math.ceil(1.0 / entry.getValue().getValue()));
                input.setCount(Util.stepsRequiredToMatch(1.0f, entry.getValue().getValue()));
                inputs.add(input);
            }
            // Empty oredicts can result in 0 inputs.
            if(inputs.size() > 0)
                outputMap.get(output).add(inputs);
        }
        // Split the recipe up into "pages"
        for(Map.Entry<BlockInfo, List<List<ItemStack>>> entry : outputMap.entrySet()){
            for(int i = 0; i < entry.getValue().size(); i+=21){
                recipes.add(new CompostRecipe(entry.getKey(),
                        entry.getValue().subList(i, Math.min(i+21, entry.getValue().size()))));
            }
        }

        return recipes;
    }
}
