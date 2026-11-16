package wraith.fabricaeexnihilo.compatibility.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import wraith.fabricaeexnihilo.modules.ModBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * JEI category layout for sieve recipes.
 *
 * <p>Left column: the block being sieved, the mesh, and (when applicable) a
 * water bucket marking a waterlogged sieve. The right grid holds every possible
 * output; each output slot carries a tooltip with its per-roll drop chances.
 *
 * <p>Slot coordinates are the top-left corner of an 18x18 slot. The grid width is
 * derived from the column count and the panel is sized around the largest recipe
 * so slots never overlap the panel border.
 *
 * <p>Every slot, including the outputs, uses the 18x18 standard slot background.
 * JEI's output slot background is 26x26 in JEI 27 (it matches the enlarged vanilla
 * output slot introduced in 1.21.9), with a (-5, -5) offset, so it is only meant
 * for a single isolated output slot. Drawing it once per cell of an 18px grid made
 * neighbouring backgrounds overlap by 8px and every output look oversized.
 */
public class SieveRecipeCategory implements IRecipeCategory<JeiSieveRecipe> {

    private static final int SLOT = 18;
    private static final int PADDING = 4;

    /** Left column: input block, mesh and optional water bucket, 3 rows. */
    private static final int LEFT_X = PADDING + 2;
    private static final int LEFT_TOP = PADDING + 12;

    /** Output grid starts after the left column plus a one-slot gap. */
    private static final int OUTPUT_X = LEFT_X + SLOT + 12;
    private static final int OUTPUT_TOP = PADDING + 6;
    private static final int OUTPUT_COLUMNS = 6;

    /** Tallest known group is 28 outputs (5 rows); keep room for 6 rows. */
    private static final int MIN_OUTPUT_ROWS = 2;

    private static final int WIDTH = OUTPUT_X + OUTPUT_COLUMNS * SLOT + PADDING + 2;

    private final IDrawable icon;

    public SieveRecipeCategory(IGuiHelper guiHelper) {
        ItemStack iconStack = ModBlocks.SIEVES.values().stream()
                .findFirst()
                .map(ItemStack::new)
                .orElseGet(() -> new ItemStack(Items.DIRT));
        this.icon = guiHelper.createDrawableItemStack(iconStack);
    }

    @Override
    public IRecipeType<JeiSieveRecipe> getRecipeType() {
        return FENJeiPlugin.SIEVE;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("fabricaeexnihilo.jei.category.sieve");
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        // JEI asks for the height once per category, so size for the largest recipe.
        return heightFor(maxOutputCount());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JeiSieveRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(LEFT_X, LEFT_TOP)
                .addIngredients(recipe.input())
                .setStandardSlotBackground()
                .addRichTooltipCallback((view, tooltip) ->
                        tooltip.add(Text.translatable("fabricaeexnihilo.jei.sieve.input")));

        builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, LEFT_X, LEFT_TOP + SLOT + 6)
                .addItemStack(recipe.mesh())
                .setStandardSlotBackground()
                .addRichTooltipCallback((view, tooltip) ->
                        tooltip.add(Text.translatable("fabricaeexnihilo.jei.sieve.mesh")));

        if (recipe.waterlogged()) {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, LEFT_X, LEFT_TOP + (SLOT + 6) * 2)
                    .addItemStack(new ItemStack(Items.WATER_BUCKET))
                    .setStandardSlotBackground()
                    .addRichTooltipCallback((view, tooltip) ->
                            tooltip.add(Text.translatable("fabricaeexnihilo.jei.sieve.waterlogged")));
        }

        List<JeiSieveRecipe.Output> outputs = recipe.outputs();
        for (int i = 0; i < outputs.size(); i++) {
            JeiSieveRecipe.Output output = outputs.get(i);
            int x = OUTPUT_X + (i % OUTPUT_COLUMNS) * SLOT;
            int y = OUTPUT_TOP + (i / OUTPUT_COLUMNS) * SLOT;
            List<Double> chances = output.chances();
            builder.addOutputSlot(x, y)
                    .addItemStack(output.stack())
                    .setStandardSlotBackground()
                    .addRichTooltipCallback((view, tooltip) ->
                            tooltip.add(Text.translatable(
                                    "fabricaeexnihilo.jei.sieve.chance", formatChances(chances))));
        }
    }

    /** Height in pixels for a grid holding {@code outputs} entries. */
    private static int heightFor(int outputs) {
        int rows = Math.max(MIN_OUTPUT_ROWS, rows(outputs));
        int gridBottom = OUTPUT_TOP + rows * SLOT;
        int leftBottom = LEFT_TOP + (SLOT + 6) * 3;
        return Math.max(gridBottom, leftBottom) + PADDING;
    }

    private static int rows(int outputs) {
        return (outputs + OUTPUT_COLUMNS - 1) / OUTPUT_COLUMNS;
    }

    /**
     * Largest group size across all registered recipes. Cached after the first
     * calculation because the recipe list only changes when JEI rebuilds.
     */
    private static int maxOutputCount() {
        return MaxOutputsHolder.MAX;
    }

    private static final class MaxOutputsHolder {
        private static final int MAX = computeMax();

        private static int computeMax() {
            int max = 0;
            for (JeiSieveRecipe recipe : SieveRecipeCollector.collect()) {
                max = Math.max(max, recipe.outputs().size());
            }
            return max;
        }
    }

    /** Formats the independent roll chances, e.g. {@code 40.0% / 25.0%}. */
    private static String formatChances(List<Double> chances) {
        List<String> parts = new ArrayList<>(chances.size());
        for (double chance : chances) {
            parts.add(String.format(Locale.ROOT, "%.1f%%", chance * 100));
        }
        return String.join(" / ", parts);
    }
}
