package wraith.fabricaeexnihilo.compatibility.rei.crucible;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public class CrucibleHeatCategory implements DisplayCategory<CrucibleHeatDisplay> {
    
    @Override
    public CategoryIdentifier<? extends CrucibleHeatDisplay> getCategoryIdentifier() {
        return PluginEntry.CRUCIBLE_HEAT;
    }
    
    @Override
    public Renderer getIcon() {
        return ItemUtils.asREIEntry(ItemUtils.getExNihiloItemStack("stone_crucible"));
    }
    
    @Override
    public Text getTitle() {
        return new LiteralText("Heat");
    }
    
    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }
    
    @Override
    public int getDisplayWidth(CrucibleHeatDisplay display) {
        return WIDTH;
    }
    
    @Override
    public List<Widget> setupDisplay(CrucibleHeatDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));
        
        var inputs = display.getInputEntries().get(0);
        var heat = display.getHeat();
        
        // Block
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + BLOCK_X, bounds.getMinY() + BLOCK_Y)).entries(inputs));
        // Heat Value
        var text = Widgets.createLabel(new Point(0, 0), new LiteralText(String.valueOf(heat)));
        text.setPoint(new Point(bounds.getMinX() + WIDTH - MARGIN - text.getBounds().getMaxX(), bounds.getMinY() + MARGIN + 18 - text.getBounds().getMaxY() - text.getBounds().getMaxY() / 2));
        
        widgets.add(text);
        
        return widgets;
    }
    
    public static final int MARGIN = 6;
    public static final int WIDTH = MARGIN * 2 + 18 * 4;
    public static final int HEIGHT = MARGIN * 2 + 16;
    
    public static final int BLOCK_X = MARGIN;
    public static final int BLOCK_Y = MARGIN;
    
}