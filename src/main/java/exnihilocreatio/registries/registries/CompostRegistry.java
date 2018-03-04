package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.compatibility.jei.barrel.compost.CompostRecipe;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.Compostable;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.IStackInfo;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CompostRegistry extends BaseRegistryMap<Ingredient, Compostable> {

    protected final Map<Ingredient, Compostable> oreRegistry = new HashMap<>();

    public CompostRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson())
                        .registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson())
                        .create(),
                ExNihiloRegistryManager.COMPOST_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(ItemStack itemStack, float value, IBlockState state, Color color) {
        if (itemStack.isEmpty())
            return;

        Ingredient ingredient = CraftingHelper.getIngredient(itemStack);

        if (registry.keySet().stream().anyMatch(entry -> entry.test(itemStack))) {
            LogUtil.error("Compost Entry for " + itemStack.getItem().getRegistryName() + " with meta " + itemStack.getMetadata() + " already exists, skipping.");
            return;
        }
        Compostable compostable = new Compostable(value, color, new ItemInfo(state));
        register(ingredient, compostable);
    }

    public void register(Item item, int meta, float value, IBlockState state, Color color) {
        register(new ItemStack(item, 1, meta), value, state, color);
    }

    public void register(Block block, int meta, float value, IBlockState state, Color color) {
        register(new ItemStack(block, 1, meta), value, state, color);
    }

    public void register(IStackInfo item, float value, IBlockState state, Color color) {
        register(item.getItemStack(), value, state, color);
    }

    public void register(ResourceLocation location, int meta, float value, IBlockState state, Color color) {
        register(ForgeRegistries.ITEMS.getValue(location), meta, value, state, color);
    }

    public void register(String name, float value, IBlockState state, Color color) {
        Ingredient ingredient = CraftingHelper.getIngredient(name);
        if (ingredient == null || ingredient.getMatchingStacks().length == 0)
            return;

        Compostable compostable = new Compostable(value, color, new ItemInfo(state));

        if (oreRegistry.keySet().stream().anyMatch(entry -> entry.getValidItemStacksPacked().equals(ingredient.getValidItemStacksPacked())))
            LogUtil.error("Compost Ore Entry for " + name + " already exists, skipping.");
        else oreRegistry.put(ingredient, compostable);
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

    public Compostable getItem(IStackInfo info) {
        return getItem(info.getItemStack());
    }

    public boolean containsItem(Item item, int meta) {
        return containsItem(new ItemStack(item, meta));
    }

    public boolean containsItem(ItemStack stack) {
        return registry.keySet().stream().anyMatch(entry -> entry.test(stack)) || oreRegistry.keySet().stream().anyMatch(entry -> entry.test(stack));
    }

    public boolean containsItem(IStackInfo info) {
        return containsItem(info.getItemStack());
    }

    @SideOnly(Side.CLIENT)
    public void recommendAllFood(File file) {
        if (FMLCommonHandler.instance().getSide().isServer()) {
            return;
        }

        IBlockState dirt = Blocks.DIRT.getDefaultState();
        Color brown = new Color("7F3F0F");

        Map<String, Compostable> recommended = Maps.newHashMap();

        for (Item item : Item.REGISTRY) {
            if (item instanceof ItemFood) {
                ItemFood food = (ItemFood) item;

                NonNullList<ItemStack> stacks = NonNullList.create();
                food.getSubItems(CreativeTabs.FOOD, stacks);

                for (ItemStack foodStack : stacks) {
                    ItemInfo foodItemInfo = new ItemInfo(foodStack);

                    if (!containsItem(foodStack)) {
                        int hungerRestored = food.getHealAmount(foodStack);

                        recommended.put(foodItemInfo.toString(), new Compostable(hungerRestored * 0.025F, brown, new BlockInfo(dirt)));
                    }
                }
            }
        }

        String json = gson.toJson(recommended, new TypeToken<Map<String, Compostable>>() {
        }.getType());

        try {
            Files.write(file.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        Map<String, Compostable> gsonInput = gson.fromJson(fr, new TypeToken<Map<String, Compostable>>() {
        }.getType());

        for (Map.Entry<String, Compostable> entry : gsonInput.entrySet()) {
            ItemInfo item = new ItemInfo(entry.getKey());
            if (registry.keySet().stream().anyMatch(ingredient -> ingredient.test(item.getItemStack())))
                LogUtil.error("Compost JSON Entry for " + item.getItemStack().getDisplayName() + " already exists, skipping.");
            else registry.put(CraftingHelper.getIngredient(item.getItemStack()), entry.getValue());
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
        List<CompostRecipe> compostRecipes = Lists.newLinkedList();

        getRegistry().forEach((key, value) -> {
            IStackInfo compostBlock = value.getCompostBlock();
            List<ItemStack> compostables = Lists.newLinkedList();
            int compostCount = (int) Math.ceil(1.0F / value.getValue());
            Stream.of(key.getMatchingStacks()).forEach(stack -> {
                if (compostables.stream().noneMatch(stack::isItemEqual)) {
                    ItemStack copy = stack.copy();
                    copy.setCount(compostCount);
                    compostables.add(copy);
                }
            });

            CompostRecipe recipe = compostRecipes.stream()
                    .filter(compostRecipe -> compostRecipe.outputMatch(compostBlock.getItemStack())
                            && compostRecipe.isNonFull())
                    .findFirst()
                    .orElse(null);
            if (recipe == null) {
                recipe = new CompostRecipe(compostBlock, Lists.newLinkedList());
                compostRecipes.add(recipe);
            }

            //This acts as a safety net, auto creating new recipes if the input list is larger than 45
            for (ItemStack stack : compostables) {
                if (recipe.isNonFull()) {
                    recipe.getInputs().add(stack);
                } else {
                    recipe = new CompostRecipe(compostBlock, Lists.newLinkedList());
                    recipe.getInputs().add(stack);
                    compostRecipes.add(recipe);
                }
            }
        });

        return compostRecipes;
    }
}
