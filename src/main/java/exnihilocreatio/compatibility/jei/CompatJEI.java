package exnihilocreatio.compatibility.jei;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModBlocks;
import exnihilocreatio.ModItems;
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
import exnihilocreatio.modules.TinkersConstruct;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.LogUtil;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

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
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(new SieveRecipeCategory(guiHelper));
        registry.addRecipeCategories(new HammerRecipeCategory(guiHelper));
        registry.addRecipeCategories(new FluidOnTopRecipeCategory(guiHelper));
        registry.addRecipeCategories(new FluidTransformRecipeCategory(guiHelper));
        registry.addRecipeCategories(new FluidBlockTransformRecipeCategory(guiHelper));
        registry.addRecipeCategories(new CompostRecipeCategory(guiHelper));
        registry.addRecipeCategories(new CrucibleRecipeCategory(guiHelper));
        registry.addRecipeCategories(new CrucibleHeatSourceRecipeCategory(guiHelper));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void register(@Nonnull IModRegistry registry) {
        LogUtil.info("ModConfig Loaded: " + ExNihiloCreatio.configsLoaded);

        if (!ExNihiloCreatio.configsLoaded) {
            ExNihiloCreatio.loadConfigs();
        }

        registerCompost(registry);
        registerFluidBlockTransform(registry);
        registerFluidOnTop(registry);
        registerFluidTransform(registry);
        registerHammer(registry);
        registerHeat(registry);
        registerSieve(registry);
        registerStoneCrucible(registry);
    }

    private void registerCompost(@Nonnull IModRegistry registry) {
        List<CompostRecipe> compostRecipes = ExNihiloRegistryManager.COMPOST_REGISTRY.getRecipeList();

        registry.addRecipes(compostRecipes, CompostRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelWood), CompostRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelStone), CompostRecipeCategory.UID);
        LogUtil.info("JEI: Compost Recipe Pages Loaded:         " + compostRecipes.size());
    }


    private void registerFluidBlockTransform(@Nonnull IModRegistry registry) {
        List<FluidBlockTransformRecipe> fluidBlockTransformRecipes = ExNihiloRegistryManager.FLUID_BLOCK_TRANSFORMER_REGISTRY.getRecipeList();

        registry.addRecipes(fluidBlockTransformRecipes, FluidBlockTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelWood), FluidBlockTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelStone), FluidBlockTransformRecipeCategory.UID);
        LogUtil.info("JEI: Fluid Block Transform Recipes Loaded:       " + fluidBlockTransformRecipes.size());
    }

    private void registerFluidOnTop(@Nonnull IModRegistry registry) {
        List<FluidOnTopRecipe> fluidOnTopRecipes = ExNihiloRegistryManager.FLUID_ON_TOP_REGISTRY.getRecipeList();

        registry.addRecipes(fluidOnTopRecipes, FluidOnTopRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelWood), FluidOnTopRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelStone), FluidOnTopRecipeCategory.UID);
        LogUtil.info("JEI: Fluid On Top Recipes Loaded:       " + fluidOnTopRecipes.size());
    }

    private void registerFluidTransform(@Nonnull IModRegistry registry) {
        List<FluidTransformRecipe> fluidTransformRecipes = ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY.getRecipeList();

        registry.addRecipes(fluidTransformRecipes, FluidTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelWood), FluidTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelStone), FluidTransformRecipeCategory.UID);
        LogUtil.info("JEI: Fluid Transform Recipes Loaded:    " + fluidTransformRecipes.size());
    }

    private void registerHammer(@Nonnull IModRegistry registry) {
        List<HammerRecipe> hammerRecipes = ExNihiloRegistryManager.HAMMER_REGISTRY.getRecipeList();

        registry.addRecipes(hammerRecipes, HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.hammerWood), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.hammerGold), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.hammerStone), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.hammerIron), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.hammerDiamond), HammerRecipeCategory.UID);

        if (Loader.isModLoaded("tconstruct")) {
            registerTinkersSledgeHammerCatalyst(registry);
        }
        LogUtil.info("JEI: Hammer Recipes Loaded:             " + hammerRecipes.size());
    }

    @Optional.Method(modid="tconstruct")
    private void registerTinkersSledgeHammerCatalyst(@Nonnull IModRegistry registry){
        registry.addRecipeCatalyst(new ItemStack(TinkersConstruct.SLEDGE_HAMMER), HammerRecipeCategory.UID);
    }

    private void registerHeat(@Nonnull IModRegistry registry) {
        List<HeatSourcesRecipe> heatSources = ExNihiloRegistryManager.HEAT_REGISTRY.getRecipeList();

        registry.addRecipes(heatSources, CrucibleHeatSourceRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.crucibleStone, 1, 1), CrucibleHeatSourceRecipeCategory.UID);

        LogUtil.info("JEI: Heat Sources Loaded:             " + heatSources.size());
    }

    private void registerSieve(@Nonnull IModRegistry registry) {
        List<SieveRecipe> sieveRecipes = ExNihiloRegistryManager.SIEVE_REGISTRY.getRecipeList();

        registry.addRecipes(sieveRecipes, SieveRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.sieve), SieveRecipeCategory.UID);
        LogUtil.info("JEI: Sieve Recipes Loaded:              " + sieveRecipes.size());
    }

    private void registerStoneCrucible(@Nonnull IModRegistry registry) {
        List<CrucibleRecipe> crucibleRecipes = ExNihiloRegistryManager.CRUCIBLE_STONE_REGISTRY.getRecipeList();

        registry.addRecipes(crucibleRecipes, CrucibleRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.crucibleStone, 1, 1), CrucibleRecipeCategory.UID);
        LogUtil.info("JEI: Stone Crucible Recipes Loaded:       " + crucibleRecipes.size());
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
    }
}
