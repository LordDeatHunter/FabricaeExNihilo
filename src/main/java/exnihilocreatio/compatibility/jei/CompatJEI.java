package exnihilocreatio.compatibility.jei;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModBlocks;
import exnihilocreatio.ModItems;
import exnihilocreatio.blocks.BlockSieve.MeshType;
import exnihilocreatio.compatibility.jei.barrel.compost.CompostRecipe;
import exnihilocreatio.compatibility.jei.barrel.compost.CompostRecipeCategory;
import exnihilocreatio.compatibility.jei.barrel.fluidblocktransform.FluidBlockTransformRecipe;
import exnihilocreatio.compatibility.jei.barrel.fluidblocktransform.FluidBlockTransformRecipeCategory;
import exnihilocreatio.compatibility.jei.barrel.fluidontop.FluidOnTopRecipe;
import exnihilocreatio.compatibility.jei.barrel.fluidontop.FluidOnTopRecipeCategory;
import exnihilocreatio.compatibility.jei.barrel.fluidtransform.FluidTransformRecipe;
import exnihilocreatio.compatibility.jei.barrel.fluidtransform.FluidTransformRecipeCategory;
import exnihilocreatio.compatibility.jei.crucible.CrucibleHeatSourceRecipeCategory;
import exnihilocreatio.compatibility.jei.crucible.CrucibleRecipe;
import exnihilocreatio.compatibility.jei.crucible.CrucibleRecipeCategory;
import exnihilocreatio.compatibility.jei.crucible.HeatSourcesRecipe;
import exnihilocreatio.compatibility.jei.hammer.HammerRecipe;
import exnihilocreatio.compatibility.jei.hammer.HammerRecipeCategory;
import exnihilocreatio.compatibility.jei.sieve.SieveRecipe;
import exnihilocreatio.compatibility.jei.sieve.SieveRecipeCategory;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.*;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.LogUtil;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JEIPlugin
public class CompatJEI implements IModPlugin {

    @Override
    public void registerItemSubtypes(@Nonnull ISubtypeRegistry subtypeRegistry) {
    }

    @Override
    public void registerIngredients(@Nonnull IModIngredientRegistration registry) {
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new SieveRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new HammerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FluidOnTopRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FluidTransformRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FluidBlockTransformRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CompostRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CrucibleRecipeCategory(registry.getJeiHelpers().getGuiHelper()));


        registry.addRecipeCategories(new CrucibleHeatSourceRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void register(@Nonnull IModRegistry registry) {
        LogUtil.info("ModConfig Loaded: " + ExNihiloCreatio.configsLoaded);
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();


        if (!ExNihiloCreatio.configsLoaded) {
            ExNihiloCreatio.loadConfigs();
        }

        //region >>>> SIEVE RECIPES
        List<SieveRecipe> sieveRecipes = Lists.newArrayList();

        for (Ingredient ingredient : ExNihiloRegistryManager.SIEVE_REGISTRY.getRegistry().keySet()) {
            for (MeshType type : MeshType.values()) {
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
        registry.addRecipes(sieveRecipes, SieveRecipeCategory.UID);

        registry.addRecipeCatalyst(new ItemStack(ModBlocks.sieve), SieveRecipeCategory.UID);
        //endregion

        //region >>>> HAMMER RECIPES
        List<HammerRecipe> hammerRecipes = Lists.newArrayList();

        for (Ingredient ingredient : ExNihiloRegistryManager.HAMMER_REGISTRY.getRegistry().keySet()) {
            HammerRecipe recipe = new HammerRecipe(ingredient);

            // If there's an input block, and at least one output
            if (!recipe.getInputs().isEmpty() && !recipe.getOutputs().isEmpty()) {
                hammerRecipes.add(recipe);
            }
        }

        registry.addRecipes(hammerRecipes, HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.hammerWood), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.hammerGold), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.hammerStone), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.hammerIron), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.hammerDiamond), HammerRecipeCategory.UID);
        //endregion

        //region >>>> FLUID TRANSFORM RECIPES
        List<FluidTransformRecipe> fluidTransformRecipes = Lists.newArrayList();

        for (FluidTransformer transformer : ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY.getFluidTransformers()) {
            // Make sure both fluids are registered
            if (FluidRegistry.isFluidRegistered(transformer.getInputFluid()) && FluidRegistry.isFluidRegistered(transformer.getOutputFluid())) {
                FluidTransformRecipe recipe = new FluidTransformRecipe(transformer);

                // If theres a bucket and at least one block (and an output, for consistency)
                if (recipe.getInputs().size() >= 2 && recipe.getOutputs().size() == 1) {
                    fluidTransformRecipes.add(new FluidTransformRecipe(transformer));
                }
            }
        }

        registry.addRecipes(fluidTransformRecipes, FluidTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelWood), FluidTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelStone), FluidTransformRecipeCategory.UID);
        //endregion

        //region >>>> FLUID ON TOP RECIPE
        List<FluidOnTopRecipe> fluidOnTopRecipes = Lists.newArrayList();
        for (FluidFluidBlock transformer : ExNihiloRegistryManager.FLUID_ON_TOP_REGISTRY.getRegistry()) {
            // Make sure both fluids are registered
            if (FluidRegistry.isFluidRegistered(transformer.getFluidInBarrel()) && FluidRegistry.isFluidRegistered(transformer.getFluidOnTop()) && transformer.getResult().getItem() != null) {
                FluidOnTopRecipe recipe = new FluidOnTopRecipe(transformer);

                if (recipe.getInputs().size() == 2 && recipe.getOutputs().size() == 1) {
                    fluidOnTopRecipes.add(new FluidOnTopRecipe(transformer));
                }
            }
        }

        registry.addRecipes(fluidOnTopRecipes, FluidOnTopRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelWood), FluidOnTopRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelStone), FluidOnTopRecipeCategory.UID);
        //endregionS

        //region >>>> FLUID BLOCK TRANSFORM RECIPE
        List<FluidBlockTransformRecipe> fluidBlockTransformRecipes = Lists.newArrayList();
        for (FluidBlockTransformer transformer : ExNihiloRegistryManager.FLUID_BLOCK_TRANSFORMER_REGISTRY.getRegistry()) {
            // Make sure everything's registered
            if (FluidRegistry.isFluidRegistered(transformer.getFluidName()) && transformer.getInput().getItem() != null && transformer.getOutput().getItem() != null) {
                FluidBlockTransformRecipe recipe = new FluidBlockTransformRecipe(transformer);

                if (recipe.getInputs().size() == 2 && recipe.getOutputs().size() == 1) {
                    fluidBlockTransformRecipes.add(new FluidBlockTransformRecipe(transformer));
                }
            }
        }

        registry.addRecipes(fluidBlockTransformRecipes, FluidBlockTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelWood), FluidBlockTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelStone), FluidBlockTransformRecipeCategory.UID);
        //endregionS

        //region >>>> COMPOST RECIPES

        List<CompostRecipe> compostRecipes = Lists.newArrayList();

        Map<Ingredient, Compostable> compostRegistry = ExNihiloRegistryManager.COMPOST_REGISTRY.getRegistry();
        Map<ItemInfo, List<ItemStack>> compostEntries = new HashMap<>();

        for (Map.Entry<Ingredient, Compostable> compostEntry : compostRegistry.entrySet()) {
            ItemInfo compostBlock = compostEntry.getValue().getCompostBlock();

            List<ItemStack> compostables = compostEntries.computeIfAbsent(compostBlock, k -> Lists.newArrayList());

            Ingredient compostItem = compostEntry.getKey();
            int compostCount = (int) Math.ceil(1.0F / compostEntry.getValue().getValue());

            for (ItemStack stack : compostItem.getMatchingStacks()){
                if (compostables.stream().noneMatch(entry -> entry.isItemEqual(stack))) {
                    ItemStack itemStack = stack.copy();
                    itemStack.setCount(compostCount);
                    compostables.add(itemStack);
                }
            }
        }

        for (Map.Entry<ItemInfo, List<ItemStack>> compostEntry : compostEntries.entrySet()) {
            // I heard you like lists, you I put some lists in your lists, so you can list while you list
            List<List<ItemStack>> splitList = Lists.newArrayList(ImmutableList.of(Lists.newArrayList()));

            for (ItemStack stack : compostEntry.getValue()) {
                if (splitList.get(0).size() >= 45) {
                    splitList.add(0, Lists.newArrayList());
                }

                splitList.get(0).add(stack);
            }

            for (List<ItemStack> compostInputs : Lists.reverse(splitList)) {
                compostRecipes.add(new CompostRecipe(compostEntry.getKey(), compostInputs));
            }
        }

        registry.addRecipes(compostRecipes, CompostRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelWood), CompostRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelStone), CompostRecipeCategory.UID);
        //endregion

        //region >>>> STONE CRUCIBLE RECIPES

        List<CrucibleRecipe> crucibleRecipes = Lists.newArrayList();

        Map<Ingredient, Meltable> crucibleRegistry = ExNihiloRegistryManager.CRUCIBLE_STONE_REGISTRY.getRegistry();
        Map<Fluid, List<ItemStack>> crucibleEntries = new HashMap<>();

        for (Map.Entry<Ingredient, Meltable> crucibleEntry : crucibleRegistry.entrySet()) {
            Fluid fluid = FluidRegistry.getFluid(crucibleEntry.getValue().getFluid());

            List<ItemStack> meltables = crucibleEntries.computeIfAbsent(fluid, k -> Lists.newArrayList());

            Ingredient crucibleItem = crucibleEntry.getKey();
            int crucibleCount = (int) Math.ceil(1000.0F / crucibleEntry.getValue().getAmount());

            for (ItemStack stack : crucibleItem.getMatchingStacks()){
                if (meltables.stream().noneMatch(entry -> entry.isItemEqual(stack))) {
                    ItemStack itemStack = stack.copy();
                    itemStack.setCount(crucibleCount);
                    meltables.add(itemStack);
                }
            }
        }

        for (Map.Entry<Fluid, List<ItemStack>> crucibleEntry : crucibleEntries.entrySet()) {
            // I heard you like lists, you I put some lists in your lists, so you can list while you list
            List<List<ItemStack>> splitList = Lists.newArrayList(ImmutableList.of(Lists.newArrayList()));

            for (ItemStack stack : crucibleEntry.getValue()) {
                if (splitList.get(0).size() >= 45) {
                    splitList.add(0, Lists.newArrayList());
                }

                splitList.get(0).add(stack);
            }

            for (List<ItemStack> crucibleInputs : Lists.reverse(splitList)) {
                crucibleRecipes.add(new CrucibleRecipe(crucibleEntry.getKey(), crucibleInputs));
            }
        }

        registry.addRecipes(crucibleRecipes, CrucibleRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.crucibleStone, 1, 1), CrucibleRecipeCategory.UID);
        //endregion

        //region >>>> HEAT RECIPES
        List<HeatSourcesRecipe> heatSources = Lists.newArrayList();

        Map<BlockInfo, Integer> heatRegistryRegistry = ExNihiloRegistryManager.HEAT_REGISTRY.getRegistry();

        for (Map.Entry<BlockInfo, Integer> blockInfoIntegerEntry : heatRegistryRegistry.entrySet()) {
            BlockInfo block = blockInfoIntegerEntry.getKey();

            heatSources.add(new HeatSourcesRecipe(block, blockInfoIntegerEntry.getValue()));
        }

        registry.addRecipes(heatSources, CrucibleHeatSourceRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.crucibleStone, 1, 1), CrucibleHeatSourceRecipeCategory.UID);
        //endregion

        LogUtil.info("JEI: Hammer Recipes Loaded:             " + hammerRecipes.size());
        LogUtil.info("JEI: Sieve Recipes Loaded:              " + sieveRecipes.size());
        LogUtil.info("JEI: Fluid Transform Recipes Loaded:    " + fluidTransformRecipes.size());
        LogUtil.info("JEI: Fluid On Top Recipes Loaded:       " + fluidOnTopRecipes.size());
        LogUtil.info("JEI: Compost Recipes Loaded:            " + compostRecipes.size());
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
    }
}
