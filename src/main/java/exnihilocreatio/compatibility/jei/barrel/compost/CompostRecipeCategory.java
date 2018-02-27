package exnihilocreatio.compatibility.jei.barrel.compost;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.Compostable;
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

import javax.annotation.Nonnull;
import java.util.List;

public class CompostRecipeCategory implements IRecipeCategory<CompostRecipe> {
    public static final String UID = "exnihilocreatio:compost";
    private static final ResourceLocation texture = new ResourceLocation(ExNihiloCreatio.MODID, "textures/gui/jei_compost.png");

    private final IDrawableStatic background;
    private final IDrawableStatic slotHighlight;

    private boolean hasHighlight;
    private int highlightX;
    private int highlightY;

    public CompostRecipeCategory(IGuiHelper helper) {
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
        return "Compost";
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

    private void setRecipe(IRecipeLayout layout, CompostRecipe recipe) {
        // BlockStoneAxle
        layout.getItemStacks().init(0, false, 74, 9);
        layout.getItemStacks().set(0, recipe.getOutputs().get(0));

        IFocus<?> focus = layout.getFocus();

        if (focus != null) {
            boolean mightHaveHighlight = focus.getMode() == IFocus.Mode.INPUT;
            hasHighlight = false;

            ItemStack focusStack = (ItemStack) focus.getValue();

            int slotIndex = 1;

            for (int i = 0; i < recipe.getInputs().size(); i++) {
                final int slotX = 2 + (i % 9 * 18);
                final int slotY = 36 + (i / 9 * 18);

                ItemStack inputStack = recipe.getInputs().get(i);

                layout.getItemStacks().init(slotIndex + i, true, slotX, slotY);
                layout.getItemStacks().set(slotIndex + i, inputStack);

                if (mightHaveHighlight && ItemStack.areItemsEqual(focusStack, inputStack)) {
                    highlightX = slotX;
                    highlightY = slotY;

                    hasHighlight = true;
                    mightHaveHighlight = false;
                }
            }
        }

        layout.getItemStacks().addTooltipCallback(new CompostTooltipCallback());
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull CompostRecipe recipe, @Nonnull IIngredients ingredients) {
        layout.getItemStacks().init(0, false, 74, 9);
        layout.getItemStacks().set(0, recipe.getOutputs().get(0));

        IFocus<?> focus = layout.getFocus();

        if (focus != null) {
            boolean mightHaveHighlight = focus.getMode() == IFocus.Mode.INPUT;
            hasHighlight = false;

            ItemStack focusStack = (ItemStack) focus.getValue();

            int slotIndex = 1;

            for (int i = 0; i < recipe.getInputs().size(); i++) {
                final int slotX = 2 + (i % 9 * 18);
                final int slotY = 36 + (i / 9 * 18);

                ItemStack inputStack = recipe.getInputs().get(i);

                layout.getItemStacks().init(slotIndex + i, true, slotX, slotY);
                layout.getItemStacks().set(slotIndex + i, inputStack);

                if (mightHaveHighlight && ItemStack.areItemsEqual(focusStack, inputStack)) {
                    highlightX = slotX;
                    highlightY = slotY;

                    hasHighlight = true;
                    mightHaveHighlight = false;
                }
            }
        }

        layout.getItemStacks().addTooltipCallback(new CompostTooltipCallback());

    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    private static class CompostTooltipCallback implements ITooltipCallback<ItemStack> {
        @Override
        public void onTooltip(int slotIndex, boolean input, @Nonnull ItemStack ingredient, @Nonnull List<String> tooltip) {
            if (input) {
                Compostable entry = ExNihiloRegistryManager.COMPOST_REGISTRY.getItem(ingredient);

                tooltip.add(String.format("Value: %.1f%%", 100.0F * entry.getValue()));
            }
        }
    }
}
