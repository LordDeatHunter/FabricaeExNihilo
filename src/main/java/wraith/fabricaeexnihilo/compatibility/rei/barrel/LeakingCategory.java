package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public class LeakingCategory implements DisplayCategory<LeakingDisplay> {

    @Override
    public CategoryIdentifier<? extends LeakingDisplay> getCategoryIdentifier() {
        return PluginEntry.LEAKING;
    }

    @Override
    public Renderer getIcon() {
        return ItemUtils.asREIEntry(Blocks.MOSSY_COBBLESTONE);
    }

    @Override
    public Text getTitle() {
        return new LiteralText("Barrel Leaking");
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public int getDisplayWidth(LeakingDisplay display) {
        return WIDTH;
    }

    @Override
    public List<Widget> setupDisplay(LeakingDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + ARROW_OFFSET_X, bounds.getMinY() + ARROW_OFFSET_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_U, ARROW_V));

        var inputs = display.getInputEntries();

        var targets = inputs.get(0);
        var fluids = inputs.get(1);
        var loss = display.recipe().getAmount();
        var result = display.getOutputEntries();

        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + OUTPUT_X, bounds.getMinY() + OUTPUT_Y)).entries(result.get(0)));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + BUCKET_X, bounds.getMinY() + BUCKET_Y)).entries(fluids));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + TARGET_X, bounds.getMinY() + TARGET_Y)).entries(targets));

        var bucketStack = fluids.stream().findFirst().orElse(null);
        String label;
        if (bucketStack != null && bucketStack.getValue() instanceof IBucketItem bucket) {
            var stack = ItemUtils.asStack((ItemConvertible) bucket);
            var key = bucket.libblockattributes__getFluid(stack);
            if (key != null && key.unitSet != null) {
                label = String.valueOf(loss);
            } else {
                label = "?";
            }
        } else {
            label = "?";
        }

        var text = Widgets.createLabel(new Point(0, 0), new LiteralText("-" + label));
        text.setPoint(new Point(bounds.getMaxX() - MARGIN - text.getBounds().getMaxX(), bounds.getMinY() + MARGIN + 9));
        widgets.add(text);

        return widgets;
    }

    public static final Identifier ARROW = FabricaeExNihilo.id("textures/gui/rei/glyphs.png");
    public static final int ARROW_WIDTH = 16;
    public static final int ARROW_HEIGHT = 16;
    public static final int ARROW_U = 0;
    public static final int ARROW_V = 0;

    public static final int MARGIN = 6;

    public static final int WIDTH = 8 * 18 + MARGIN * 2;
    public static final int HEIGHT = 3 * 18 + MARGIN * 2;

    // Center the arrow
    public static final int ARROW_OFFSET_X = WIDTH / 2 - 9;
    public static final int ARROW_OFFSET_Y = HEIGHT / 2 - 9;

    public static final int TARGET_X = ARROW_OFFSET_X - 18;
    public static final int TARGET_Y = ARROW_OFFSET_Y + 9;

    public static final int BUCKET_X = ARROW_OFFSET_X - 18;
    public static final int BUCKET_Y = ARROW_OFFSET_Y - 9;

    public static final int OUTPUT_X = ARROW_OFFSET_X + 18;
    public static final int OUTPUT_Y = ARROW_OFFSET_Y + 9;

}