package exnihilocreatio.compatibility.jei;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModBlocks;
import exnihilocreatio.compatibility.jei.barrel.compost.CompostRecipe;
import exnihilocreatio.compatibility.jei.barrel.compost.CompostRecipeCategory;
import exnihilocreatio.compatibility.jei.barrel.fluiditemtransform.FluidItemTransformRecipe;
import exnihilocreatio.compatibility.jei.barrel.fluiditemtransform.FluidItemTransformRecipeCategory;
import exnihilocreatio.compatibility.jei.barrel.fluidontop.FluidOnTopRecipe;
import exnihilocreatio.compatibility.jei.barrel.fluidontop.FluidOnTopRecipeCategory;
import exnihilocreatio.compatibility.jei.barrel.fluidtransform.FluidTransformRecipe;
import exnihilocreatio.compatibility.jei.barrel.fluidtransform.FluidTransformRecipeCategory;
import exnihilocreatio.compatibility.jei.crook.CrookRecipe;
import exnihilocreatio.compatibility.jei.crook.CrookRecipeCategory;
import exnihilocreatio.compatibility.jei.crucible.CrucibleHeatSourceRecipeCategory;
import exnihilocreatio.compatibility.jei.crucible.CrucibleRecipe;
import exnihilocreatio.compatibility.jei.crucible.CrucibleRecipeCategory;
import exnihilocreatio.compatibility.jei.crucible.HeatSourcesRecipe;
import exnihilocreatio.compatibility.jei.hammer.HammerRecipe;
import exnihilocreatio.compatibility.jei.hammer.HammerRecipeCategory;
import exnihilocreatio.compatibility.jei.sieve.SieveRecipe;
import exnihilocreatio.compatibility.jei.sieve.SieveRecipeCategory;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.modules.TinkersConstruct;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.ItemUtil;
import exnihilocreatio.util.LogUtil;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

@JEIPlugin
public class CompatJEI implements IModPlugin {
    private final ISubtypeRegistry.ISubtypeInterpreter interpreter = new IgnoreNBTandMetaInterpreter();

    @Override
    public void registerItemSubtypes(@Nonnull ISubtypeRegistry subtypeRegistry) {
        if(Loader.isModLoaded("tconstruct") && ModConfig.compatibility.tinkers_construct_compat.JEItinkersTools){
            if(ModConfig.compatibility.tinkers_construct_compat.addExNihiloHammer)
                subtypeRegistry.registerSubtypeInterpreter(TinkersConstruct.getHammer(), interpreter);
            if(ModConfig.compatibility.tinkers_construct_compat.addExNihiloCrook)
                subtypeRegistry.registerSubtypeInterpreter(TinkersConstruct.getCrook(), interpreter);
        }
    }

    @Override
    public void registerIngredients(@Nonnull IModIngredientRegistration registry) {
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(new SieveRecipeCategory(guiHelper));
        registry.addRecipeCategories(new HammerRecipeCategory(guiHelper));
        registry.addRecipeCategories(new CrookRecipeCategory(guiHelper));
        registry.addRecipeCategories(new FluidOnTopRecipeCategory(guiHelper));
        registry.addRecipeCategories(new FluidTransformRecipeCategory(guiHelper));
        registry.addRecipeCategories(new FluidItemTransformRecipeCategory(guiHelper));
        registry.addRecipeCategories(new CompostRecipeCategory(guiHelper));
        registry.addRecipeCategories(new CrucibleRecipeCategory(guiHelper, "exnihilocreatio:crucible_wood"));
        registry.addRecipeCategories(new CrucibleRecipeCategory(guiHelper, "exnihilocreatio:crucible_stone"));
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
        registerCrook(registry);
        registerHeat(registry);
        registerSieve(registry);
        registerStoneCrucible(registry);
        registerWoodCrucible(registry);

        // Register Crook and Hammer catalysts
        for(Item item : Item.REGISTRY){
            if(ItemUtil.isHammer(item))
                registry.addRecipeCatalyst(new ItemStack(item), HammerRecipeCategory.UID);
            if(ItemUtil.isCrook(item))
                registry.addRecipeCatalyst(new ItemStack(item), CrookRecipeCategory.UID);
        }
    }

    private void registerCompost(@Nonnull IModRegistry registry) {
        List<CompostRecipe> compostRecipes = ExNihiloRegistryManager.COMPOST_REGISTRY.getRecipeList();

        registry.addRecipes(compostRecipes, CompostRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelWood), CompostRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelStone), CompostRecipeCategory.UID);
        LogUtil.info("JEI: Compost Recipe Pages Loaded:         " + compostRecipes.size());
    }


    private void registerFluidBlockTransform(@Nonnull IModRegistry registry) {
        List<FluidItemTransformRecipe> fluidItemTransformRecipes = ExNihiloRegistryManager.FLUID_BLOCK_TRANSFORMER_REGISTRY.getRecipeList();
        fluidItemTransformRecipes.addAll(ExNihiloRegistryManager.FLUID_ITEM_FLUID_REGISTRY.getRecipeList());

        registry.addRecipes(fluidItemTransformRecipes, FluidItemTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelWood), FluidItemTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrelStone), FluidItemTransformRecipeCategory.UID);
        LogUtil.info("JEI: Fluid Item Transform Recipes Loaded:       " + fluidItemTransformRecipes.size());
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
        LogUtil.info("JEI: Hammer Recipes Loaded:             " + hammerRecipes.size());
    }

    private void registerCrook(@Nonnull IModRegistry registry) {
        List<CrookRecipe> crookRecipes = ExNihiloRegistryManager.CROOK_REGISTRY.getRecipeList();
        registry.addRecipes(crookRecipes, CrookRecipeCategory.UID);
        LogUtil.info("JEI: Crook Recipes Loaded:              " + crookRecipes.size());
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

        registry.addRecipes(crucibleRecipes, "exnihilocreatio:crucible_stone");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.crucibleStone, 1, 1), "exnihilocreatio:crucible_stone");
        LogUtil.info("JEI: Stone Crucible Recipes Loaded:       " + crucibleRecipes.size());
    }

    private void registerWoodCrucible(@Nonnull IModRegistry registry) {
        List<CrucibleRecipe> crucibleRecipes = ExNihiloRegistryManager.CRUCIBLE_WOOD_REGISTRY.getRecipeList();

        registry.addRecipes(crucibleRecipes, "exnihilocreatio:crucible_wood");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.crucibleWood, 1, 0), "exnihilocreatio:crucible_wood");
        LogUtil.info("JEI: Wood Crucible Recipes Loaded:        " + crucibleRecipes.size());
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
    }
}
