package wraith.fabricaeexnihilo.compatibility.rei.tools;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class ToolCategory implements DisplayCategory<ToolDisplay> {
    
    private final CategoryIdentifier<ToolDisplay> tool;
    private final ItemStack icon;
    private final String name;
    
    private final int GLYPH_U;
    
    public ToolCategory(CategoryIdentifier<ToolDisplay> tool, ItemStack icon, String name) {
        this.tool = tool;
        this.icon = icon;
        this.name = name;
        GLYPH_U = (tool == PluginEntry.HAMMER) ? 16 : 32;
    }
    
    @Override
    public Renderer getIcon() {
        return ItemUtils.asREIEntry(icon);
    }
    
    @Override
    public Text getTitle() {
        return new TranslatableText(this.name);
    }
    
    @Override
    public CategoryIdentifier<ToolDisplay> getCategoryIdentifier() {
        return tool;
    }
    
    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }
    
    @Override
    public int getDisplayWidth(ToolDisplay display) {
        return WIDTH;
    }
    
    @Override
    public @NotNull List<Widget> setupDisplay(ToolDisplay recipeDisplay, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));
        
        var arrowWidget = new GlyphWidget(
                bounds,
                bounds.getMinX() + ARROW_OFFSET_X,
                bounds.getMinY() + ARROW_OFFSET_Y,
                GLYPH_WIDTH,
                GLYPH_HEIGHT,
                GLYPH,
                GLYPH_U,
                GLYPH_V
        );
        widgets.add(arrowWidget);
        
        var inputs = recipeDisplay.getInputEntries();
        var outputs = recipeDisplay.getOutputEntries();
        
        // Tools
        if (inputs.size() > 1 && !inputs.get(1).isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + TOOL_X, bounds.getMinY() + TOOL_Y)).entries(inputs.get(1)));
        }
        // Target
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + BLOCK_X, bounds.getMinY() + BLOCK_Y)).entries(inputs.get(0)));
        
        for (var i = 0; i < outputs.size(); i++) {
            var output = outputs.get(i);
            widgets.add(
                    Widgets.createSlot(new Point(
                            bounds.getMinX() + OUTPUT_X + (i % OUTPUT_SLOTS_X) * 18,
                            bounds.getMinY() + OUTPUT_Y + (i / OUTPUT_SLOTS_X) * 18)).entries(output)
            );
        }
        
        // Fill in the empty spots
        for (var i = 0; i < outputs.size() && i < MAX_OUTPUTS; i++) {
            widgets.add(
                    Widgets.createSlot(new Point(
                            bounds.getMinX() + OUTPUT_X + (i % OUTPUT_SLOTS_X) * 18,
                            bounds.getMinY() + OUTPUT_Y + (i / OUTPUT_SLOTS_X) * 18))
            );
        }
        
        return widgets;
    }
    
    public static final Identifier GLYPH = id("textures/gui/rei/glyphs.png");
    
    public static final int OUTPUT_SLOTS_X = Math.max(FabricaeExNihilo.CONFIG.modules.REI.toolNumCols, 1);
    public static final int OUTPUT_SLOTS_Y = Math.max(FabricaeExNihilo.CONFIG.modules.REI.toolNumRows, 2);
    
    public static final int MARGIN = 6;
    
    public static final int TOOL_X = MARGIN;
    public static final int BLOCK_X = TOOL_X;
    public static final int ARROW_OFFSET_X = TOOL_X + 18;
    public static final int OUTPUT_X = ARROW_OFFSET_X + 18;
    public static final int WIDTH = OUTPUT_X + OUTPUT_SLOTS_X * 18 + TOOL_X;
    
    public static final int OUTPUT_Y = MARGIN;
    public static final int HEIGHT = OUTPUT_Y + OUTPUT_SLOTS_Y * 18 + OUTPUT_Y;
    
    public static final int TOOL_Y = MARGIN + (HEIGHT - 2 * MARGIN) / 2 - 18;
    public static final int BLOCK_Y = TOOL_Y + 18;
    public static final int ARROW_OFFSET_Y = MARGIN + (HEIGHT - 2 * MARGIN) / 2 - 9;
    
    public static final int GLYPH_WIDTH = 16;
    public static final int GLYPH_HEIGHT = 16;
    public static final int GLYPH_V = 0;
    
    public static final int MAX_OUTPUTS = OUTPUT_SLOTS_X * OUTPUT_SLOTS_Y;
    
}
