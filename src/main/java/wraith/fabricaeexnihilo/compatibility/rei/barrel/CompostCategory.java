package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class CompostCategory implements DisplayCategory<CompostDisplay> {
    
    @Override
    public CategoryIdentifier<? extends CompostDisplay> getCategoryIdentifier() {
        return PluginEntry.COMPOSTING;
    }
    
    @Override
    public Renderer getIcon() {
        return ItemUtils.asREIEntry("oak_barrel");
    }
    
    @Override
    public Text getTitle() {
        return new TranslatableText("Barrel Composting");
    }
    
    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }
    
    @Override
    public int getDisplayWidth(CompostDisplay display) {
        return WIDTH;
    }
    
    @Override
    public List<Widget> setupDisplay(CompostDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));
        
        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + ARROW_OFFSET_X, bounds.getMinY() + ARROW_OFFSET_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_U, ARROW_V));
        
        var inputs = display.getInputEntries().get(0);
        var outputs = display.getOutputEntries();
        
        // Output
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + BLOCK_X, bounds.getMinY() + BLOCK_Y)).entries(outputs.get(0)));
        
        // Input
        for (int i = 0; i < inputs.size(); i++) {
            var output = inputs.get(i);
            widgets.add(
                    Widgets.createSlot(new Point(
                            bounds.getMinX() + INPUT_X + (i % INPUT_SLOTS_X) * 18,
                            bounds.getMinY() + INPUT_Y + (i / INPUT_SLOTS_X) * 18)).entry(output));
            
        }
        
        // Fill in the empty spots
        for (int i = 0; i < inputs.size() && i < MAX_INPUT; i++) {
            widgets.add(
                    Widgets.createSlot(new Point(
                            bounds.getMinX() + INPUT_X + (i % INPUT_SLOTS_X) * 18,
                            bounds.getMinY() + INPUT_Y + (i / INPUT_SLOTS_X) * 18)));
        }
        return widgets;
    }
    
    public static Identifier ARROW = id("textures/gui/rei/glyphs.png");
    
    public static int INPUT_SLOTS_X = Math.max(FabricaeExNihilo.CONFIG.modules.REI.compostNumCols, 1);
    public static int INPUT_SLOTS_Y = Math.max(FabricaeExNihilo.CONFIG.modules.REI.compostNumRows, 1);
    
    public static int MARGIN = 6;
    
    public static int WIDTH = 2 * 18 + INPUT_SLOTS_X * 18 + MARGIN * 2;
    public static int HEIGHT = INPUT_SLOTS_Y * 18 + MARGIN * 2;
    
    public static int BLOCK_X = MARGIN;
    public static int ARROW_OFFSET_X = BLOCK_X + 18;
    public static int INPUT_X = ARROW_OFFSET_X + 18;
    
    public static int BLOCK_Y = MARGIN + (HEIGHT - 2 * MARGIN) / 2 - 9;
    public static int ARROW_OFFSET_Y = BLOCK_Y;
    public static int INPUT_Y = MARGIN;
    
    public static int ARROW_WIDTH = 16;
    public static int ARROW_HEIGHT = 16;
    public static int ARROW_U = 0;
    public static int ARROW_V = 16;
    
    public static int MAX_INPUT = INPUT_SLOTS_X * INPUT_SLOTS_Y;
    
}
