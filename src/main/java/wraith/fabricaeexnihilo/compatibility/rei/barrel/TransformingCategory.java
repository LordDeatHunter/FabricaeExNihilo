package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;

import java.util.ArrayList;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class TransformingCategory implements DisplayCategory<TransformingDisplay> {

    public static final Identifier ARROW = id("textures/gui/rei/glyphs.png");
    public static final int MARGIN = 6;
    public static final int WIDTH = 8 * 18 + MARGIN * 2;
    public static final int BARRELS_X = WIDTH / 2 - 9;
    public static final int ARROW1_X = BARRELS_X - 18;
    public static final int ARROW2_X = BARRELS_X + 18;
    public static final int BELOW_X = BARRELS_X;
    public static final int INPUT_X = BARRELS_X - 2 * 18;
    public static final int OUTPUT_X = BARRELS_X + 2 * 18;
    public static final int HEIGHT = 2 * 18 + MARGIN * 2;
    public static final int BARRELS_Y = MARGIN;
    public static final int ARROW1_Y = BARRELS_Y;
    public static final int ARROW2_Y = BARRELS_Y;
    public static final int BELOW_Y = BARRELS_Y + 18;
    public static final int INPUT_Y = BARRELS_Y;
    public static final int OUTPUT_Y = BARRELS_Y;
    public static final int ARROW_WIDTH = 16;
    public static final int ARROW_HEIGHT = 16;
    public static final int ARROW_U = 0;
    public static final int ARROW_V = 0;

    @Override
    public CategoryIdentifier<? extends TransformingDisplay> getCategoryIdentifier() {
        return PluginEntry.TRANSFORMING;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.MYCELIUM);
    }

    @Override
    public Text getTitle() {
        return new TranslatableText("Transformation");
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public int getDisplayWidth(TransformingDisplay display) {
        return WIDTH;
    }

    @Override
    public List<Widget> setupDisplay(TransformingDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + ARROW1_X, bounds.getMinY() + ARROW1_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_U, ARROW_V));
        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + ARROW2_X, bounds.getMinY() + ARROW2_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_U, ARROW_V));

        var inputs = display.getInputEntries();
        var inBarrel = inputs.get(0);
        var below = inputs.get(1);
        var barrels = inputs.get(2);
        var outputs = display.getOutputEntries().get(0);

        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + INPUT_X, bounds.getMinY() + INPUT_Y)).entries(inBarrel));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + BELOW_X, bounds.getMinY() + BELOW_Y)).entries(below));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + BARRELS_X, bounds.getMinY() + BARRELS_Y)).entries(barrels));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + OUTPUT_X, bounds.getMinY() + OUTPUT_Y)).entries(outputs));

        return widgets;
    }

}