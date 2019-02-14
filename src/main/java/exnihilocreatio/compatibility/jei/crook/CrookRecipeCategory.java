package exnihilocreatio.compatibility.jei.crook;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.CrookReward;
import exnihilocreatio.util.ItemUtil;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class CrookRecipeCategory implements IRecipeCategory<CrookRecipe> {
    public static final String UID = "exnihilocreatio:crook";
    private static final ResourceLocation texture = new ResourceLocation(ExNihiloCreatio.MODID, "textures/gui/jei_mini.png");

    private final IDrawableStatic background;
    private final IDrawableStatic slotHighlight;

    public CrookRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(texture, 0, 40, 166, 22).build();
        this.slotHighlight = guiHelper.createDrawable(texture, 166, 0, 18, 18);
    }

    @Override
    @Nonnull
    public String getUid() {
        return UID;
    }

    @Override
    @Nonnull
    public String getTitle() {
        return "Crook";
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

    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull CrookRecipe recipeWrapper, @Nonnull IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 2, 2);
        recipeLayout.getItemStacks().set(0, recipeWrapper.getInputs());

        IFocus<?> focus = recipeLayout.getFocus();

        int slotIndex = 1;
        for (int i = 0; i < recipeWrapper.getOutputs().size(); i++) {
            final int slotX = 38 + (i * 18);

            ItemStack outputStack = recipeWrapper.getOutputs().get(i);

            recipeLayout.getItemStacks().init(slotIndex + i, false, slotX, 2);
            recipeLayout.getItemStacks().set(slotIndex + i, outputStack);

            if (focus != null) {
                ItemStack focusStack = (ItemStack) focus.getValue();
                if (focus.getMode() == IFocus.Mode.OUTPUT
                        && !focusStack.isEmpty()
                        && focusStack.getItem() == outputStack.getItem()
                        && focusStack.getItemDamage() == outputStack.getItemDamage()) {
                    recipeLayout.getItemStacks().setBackground(i+slotIndex,slotHighlight);
                }
            }
        }


        recipeLayout.getItemStacks().addTooltipCallback(new CrookTooltipCallback(recipeWrapper));
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    private static class CrookTooltipCallback implements ITooltipCallback<ItemStack> {
        private final CrookRecipe recipe;

        private CrookTooltipCallback(CrookRecipe recipeWrapper) {
            this.recipe = recipeWrapper;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void onTooltip(int slotIndex, boolean input, @Nonnull ItemStack ingredient, @Nonnull List<String> tooltip) {
            if (!input) {
                ItemStack blockStack = recipe.getInputs().get(0);
                Block blockBase = Block.getBlockFromItem(blockStack.getItem());

                @SuppressWarnings("deprecation")
                IBlockState block = blockBase.getStateFromMeta(blockStack.getMetadata());

                List<CrookReward> allRewards = ExNihiloRegistryManager.CROOK_REGISTRY.getRewards(block);

                allRewards.removeIf(reward -> !ItemUtil.areStacksEquivalent(reward.getStack(), ingredient));

                for (CrookReward reward : allRewards) {
                    float chance = 100.0F * reward.getChance();

                    String format = chance >= 10 ? " - %3.0f%% (x%d)" : "%1.1f%% - (x%d)";

                    tooltip.add(String.format(format, chance, reward.getStack().getCount()));
                }
            }
        }
    }
}
