package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.compatibility.jei.crucible.CrucibleRecipe;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomIngredientJson;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.manager.IDefaultRecipeProvider;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.Meltable;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.LogUtil;
import exnihilocreatio.util.OreIngredientStoring;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CrucibleRegistry extends BaseRegistryMap<Ingredient, Meltable> {
    protected final Map<Ingredient, Meltable> oreRegistry = new HashMap<>();

    public CrucibleRegistry(List<? extends IDefaultRecipeProvider> defaultRecipeProviders) {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson())
                        .registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson())
                        .registerTypeAdapter(Ingredient.class, new CustomIngredientJson())
                        .registerTypeAdapter(OreIngredientStoring.class, new CustomIngredientJson())
                        .enableComplexMapKeySerialization()
                        .create(),
                defaultRecipeProviders
        );
    }

    public void register(ItemInfo item, Fluid fluid, int amount) {
        register(item.getItemStack(), fluid, amount);
    }

    public void register(ItemInfo item, Meltable meltable) {
        register(item.getItemStack(), meltable);
    }

    public void register(BlockInfo block, Fluid fluid, int amount) {
        register(block.getItemStack(), fluid, amount);
    }

    public void register(BlockInfo block, Meltable meltable) {
        register(block.getItemStack(), meltable);
    }

    public void register(ItemStack stack, Fluid fluid, int amount) {
        register(stack, new Meltable(fluid.getName(), amount));
    }

    public void register(ItemStack stack, Meltable meltable) {
        if (stack.isEmpty() || !FluidRegistry.isFluidRegistered(meltable.getFluid())) return;
        if (registry.keySet().stream().anyMatch(ingredient -> ingredient.test(stack)))
            LogUtil.warn("Crucible entry for " + stack.getDisplayName() + " with meta " + stack.getMetadata() + " already exists, skipping.");
        else register(CraftingHelper.getIngredient(stack), meltable);
    }

    public void register(String name, Fluid fluid, int amount) {
        register(name, new Meltable(fluid.getName(), amount));
    }

    public void register(String name, Meltable meltable) {
        Ingredient ingredient = new OreIngredientStoring(name);
        if (ingredient.getMatchingStacks().length == 0 || !FluidRegistry.isFluidRegistered(meltable.getFluid()))
            return;

        if (oreRegistry.keySet().stream().anyMatch(entry -> entry.getValidItemStacksPacked().equals(ingredient.getValidItemStacksPacked())))
            LogUtil.error("Crucible Ore Entry for " + name + " already exists, skipping.");
        else oreRegistry.put(ingredient, meltable);
    }

    public boolean canBeMelted(ItemStack stack) {
        return registry.keySet().stream().anyMatch(entry -> entry.test(stack)) || oreRegistry.keySet().stream().anyMatch(entry -> entry.test(stack));
    }

    public boolean canBeMelted(ItemInfo info) {
        return canBeMelted(info.getItemStack());
    }

    public boolean canBeMelted(Item item, int meta) {
        return canBeMelted(new ItemStack(item, meta));
    }

    public Meltable getMeltable(ItemStack stack) {
        Ingredient ingredient = registry.keySet().stream().filter(entry -> entry.test(stack)).findFirst().orElse(null);
        if (ingredient != null) return registry.get(ingredient);
        ingredient = oreRegistry.keySet().stream().filter(entry -> entry.test(stack)).findFirst().orElse(null);
        if (ingredient != null) return oreRegistry.get(ingredient);
        else return Meltable.EMPTY;
    }

    public Meltable getMeltable(ItemInfo info) {
        return getMeltable(info.getItemStack());
    }

    public Meltable getMeltable(Item item, int meta) {
        return getMeltable(new ItemStack(item, meta));
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        Map<String, Meltable> gsonInput = gson.fromJson(fr, new TypeToken<Map<String, Meltable>>() {
        }.getType());

        gsonInput.forEach((key, value) -> {
            ItemInfo item = new ItemInfo(key);
            if (registry.keySet().stream().anyMatch(ingredient -> ingredient.test(item.getItemStack())))
                LogUtil.error("Compost JSON Entry for " + item.getItemStack().getDisplayName() + " already exists, skipping.");
            else registry.put(CraftingHelper.getIngredient(item.getItemStack()), value);
        });
    }

    @Override
    public Map<Ingredient, Meltable> getRegistry() {
        //noinspection unchecked
        Map<Ingredient, Meltable> map = (HashMap) ((HashMap) registry).clone();
        map.putAll(oreRegistry);
        return map;
    }

    @Override
    public List<CrucibleRecipe> getRecipeList() {
        List<CrucibleRecipe> crucibleRecipes = Lists.newLinkedList();

        getRegistry().forEach((crucibleItem, meltable) -> {
            Fluid fluid = FluidRegistry.getFluid(meltable.getFluid());
            List<ItemStack> meltables = Lists.newLinkedList();
            int crucibleCount = (int) Math.ceil(1000.0F / meltable.getAmount());
            Stream.of(crucibleItem.getMatchingStacks()).forEach(stack -> {
                if (meltables.stream().noneMatch(stack::isItemEqual)) {
                    ItemStack copy = stack.copy();
                    copy.setCount(crucibleCount);
                    meltables.add(copy);
                }
            });
            ItemStack fluidBucket = FluidUtil.getFilledBucket(new FluidStack(fluid, 1000));
            CrucibleRecipe recipe = crucibleRecipes.stream()
                    .filter(crucibleRecipe -> crucibleRecipe.getFluid().isItemEqual(fluidBucket)
                            && crucibleRecipe.isNonFull())
                    .findFirst()
                    .orElse(null);
            if (recipe == null) {
                recipe = new CrucibleRecipe(fluid, Lists.newLinkedList());
                crucibleRecipes.add(recipe);
            }

            //This acts as a safety net, auto creating new recipes if the input list is larger than 45
            for (ItemStack stack : meltables) {
                if (recipe.isNonFull())
                    recipe.getInputs().add(stack);
                else {
                    recipe = new CrucibleRecipe(fluid, Lists.newLinkedList());
                    recipe.getInputs().add(stack);
                    crucibleRecipes.add(recipe);
                }
            }
        });

        return crucibleRecipes;
    }
}
