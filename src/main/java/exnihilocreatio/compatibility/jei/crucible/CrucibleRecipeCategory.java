package exnihilocreatio.compatibility.jei.crucible;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.Meltable;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class CrucibleRecipeCategory implements IRecipeCategory<CrucibleRecipe> {
    public static final String UID = "exnihilocreatio:crucible";
    private static final ResourceLocation texture = new ResourceLocation(ExNihiloCreatio.MODID, "textures/gui/jei_crucible.png");

    private final IDrawableStatic background;
    private final IDrawableStatic slotHighlight;

    private boolean hasHighlight;
    private int highlightX;
    private int highlightY;

    public CrucibleRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(texture, 0, 0, 166, 128);
        this.slotHighlight = helper.createDrawable(texture, 166, 0, 18, 18);
    }

    @Override
    @Nonnull
    public String getUid() {
        return UID;
    }

    @Override
    @Nonnull
    public String getTitle() {
        return "Crucible";
    }

    @Override
    public String getModName() {
        return ExNihiloCreatio.MODID;
    }

    @Override
    @Nonnull
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        if (hasHighlight) {
            slotHighlight.draw(minecraft, highlightX, highlightY);
        }
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull CrucibleRecipe recipeWrapper, @Nonnull IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 74, 9);
        recipeLayout.getItemStacks().set(0, recipeWrapper.getFluid());

        IFocus<?> focus = recipeLayout.getFocus();

        boolean mightHaveHighlight = false;
        hasHighlight = false;

        if (focus != null) {
            mightHaveHighlight = focus.getMode() == IFocus.Mode.INPUT;
        }


            int slotIndex = 1;

            for (int i = 0; i < recipeWrapper.getInputs().size(); i++) {
                final int slotX = 2 + (i % 9 * 18);
                final int slotY = 36 + (i / 9 * 18);

                ItemStack inputStack = recipeWrapper.getInputs().get(i);

                recipeLayout.getItemStacks().init(slotIndex + i, true, slotX, slotY);
                recipeLayout.getItemStacks().set(slotIndex + i, inputStack);

                if (mightHaveHighlight && ItemStack.areItemsEqual((ItemStack) focus.getValue(), inputStack)) {
                    highlightX = slotX;
                    highlightY = slotY;

                    hasHighlight = true;
                    mightHaveHighlight = false;
                }

        }

        recipeLayout.getItemStacks().addTooltipCallback(new CrucibleTooltipCallback());
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    private static class CrucibleTooltipCallback implements ITooltipCallback<ItemStack> {
        @Override
        @SideOnly(Side.CLIENT)
        public void onTooltip(int slotIndex, boolean input, @Nonnull ItemStack ingredient, @Nonnull List<String> tooltip) {
            if (!input) {
                Meltable entry = ExNihiloRegistryManager.CRUCIBLE_STONE_REGISTRY.getMeltable(ingredient);
                tooltip.add(String.format("Value: %.1f%%", 1000.0F / entry.getAmount()));
            }
        }
    }

}
