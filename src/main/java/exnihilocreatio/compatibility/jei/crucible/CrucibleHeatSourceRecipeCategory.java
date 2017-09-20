package exnihilocreatio.compatibility.jei.crucible;

import exnihilocreatio.ExNihiloCreatio;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.config.Constants;
import net.minecraft.util.ResourceLocation;

public class CrucibleHeatSourceRecipeCategory implements IRecipeCategory<HeatSourcesRecipe> {
    public static final String UID = "exnihilocreatio:heat_sources";
    private static final ResourceLocation texture = new ResourceLocation(ExNihiloCreatio.MODID, "textures/gui/jei_fluid_block_transform.png");

    private final IDrawableStatic background;
    private final IDrawableStatic flameTransparentBackground;

    public CrucibleHeatSourceRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(Constants.RECIPE_GUI_VANILLA, 0, 134, 18, 34, 0, 0, 0, 80);

        flameTransparentBackground = guiHelper.createDrawable(Constants.RECIPE_BACKGROUND, 215, 0, 14, 14);
    }


    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return "Crucible Heat Sources";
    }

    @Override
    public String getModName() {
        return ExNihiloCreatio.MODID;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, HeatSourcesRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStackGroup = recipeLayout.getItemStacks();

        guiItemStackGroup.init(0, true, 0, 16);
        guiItemStackGroup.set(ingredients);
    }
}
