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
    private static final ResourceLocation texture = new ResourceLocation(ExNihiloCreatio.MODID, "textures/gui/jei_mini.png");

    private final IDrawableStatic background;
    private final IDrawableStatic slotHighlight;

    public CompostRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(texture, 0, 60, 166, 22);
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
    @Nonnull
    public String getModName() {
        return ExNihiloCreatio.MODID;
    }

    @Override
    @Nonnull
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull CompostRecipe recipe, @Nonnull IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, false, 2, 2);
        recipeLayout.getItemStacks().set(0, recipe.getOutputs().get(0));

        IFocus<?> focus = recipeLayout.getFocus();

        for (int i = 1; i < recipe.getInputs().size()+1; i++) {
            final int slotX = 38 + (i - 1) * 18;

            List<ItemStack> stacks = recipe.getInputs().get(i-1);

            recipeLayout.getItemStacks().init(i, true, slotX, 2);
            recipeLayout.getItemStacks().set(i, stacks);

            if(stacks.stream().anyMatch(stack -> ItemStack.areItemsEqual((ItemStack) focus.getValue(), stack))) {
                recipeLayout.getItemStacks().setBackground(i,slotHighlight);
            }
        }

        recipeLayout.getItemStacks().addTooltipCallback(new CompostTooltipCallback());

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
