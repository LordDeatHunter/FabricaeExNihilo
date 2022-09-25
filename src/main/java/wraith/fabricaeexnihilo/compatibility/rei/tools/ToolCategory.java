package wraith.fabricaeexnihilo.compatibility.rei.tools;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;

import java.util.ArrayList;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class ToolCategory implements DisplayCategory<ToolDisplay> {

    public static final Identifier GLYPH = id("textures/gui/rei/glyphs.png");
    public static final int GLYPH_V = 0;
    private final int GLYPH_U;
    private final ItemStack icon;
    private final String name;
    private final CategoryIdentifier<ToolDisplay> tool;

    public ToolCategory(CategoryIdentifier<ToolDisplay> tool, ItemStack icon, String name) {
        this.tool = tool;
        this.icon = icon;
        this.name = name;
        GLYPH_U = (tool == PluginEntry.CRUSHING) ? 16 : 32;
    }

    @Override
    public CategoryIdentifier<ToolDisplay> getCategoryIdentifier() {
        return tool;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(icon);
    }

    @Override
    public Text getTitle() {
        return Text.translatable(this.name);
    }

    @Override
    public @NotNull List<Widget> setupDisplay(ToolDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(new GlyphWidget(bounds, startPoint.x + 31, startPoint.y + 4, 16, 16, GLYPH, GLYPH_U, GLYPH_V));

        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5)).entries(display.block).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5)).entries(display.result).disableBackground().markOutput());

        return widgets;
    }

}
