package wraith.fabricaeexnihilo.compatibility.rei.sieve;

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
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;

import java.util.ArrayList;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class SieveCategory implements DisplayCategory<SieveDisplay> {

    public static final Identifier ARROW = id("textures/gui/rei/glyphs.png");
    public static final int ARROW_HEIGHT = 16;
    public static final int ARROW_U = 0;
    public static final int ARROW_V = 0;
    public static final int ARROW_WIDTH = 16;
    public static final int MARGIN = 6;
    public static final int HEIGHT = 4 * 18 + MARGIN * 2;
    public static final int BLOCK_Y = MARGIN + (HEIGHT - 2 * MARGIN) / 2 - 27;
    public static final int MESH_Y = BLOCK_Y + 18;
    public static final int FLUID_Y = MESH_Y + 18;
    public static final int ARROW_OFFSET_Y = MESH_Y;
    public static final int OUTPUT_Y = BLOCK_Y;
    public static final int BLOCK_X = MARGIN;
    public static final int FLUID_X = MARGIN;
    public static final int MESH_X = MARGIN;
    public static final int ARROW_OFFSET_X = MESH_X + 18;
    public static final int OUTPUT_X = ARROW_OFFSET_X + 18;
    public static final int OUTPUT_SLOTS_X = 9;
    public static final int OUTPUT_SLOTS_Y = 3;
    public static final int MAX_OUTPUTS = OUTPUT_SLOTS_X * OUTPUT_SLOTS_Y;
    public static final int WIDTH = 2 * 18 + OUTPUT_SLOTS_X * 18 + MARGIN * 2;
    private final ItemStack icon;
    private final String name;

    public SieveCategory(ItemStack icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    @Override
    public CategoryIdentifier<? extends SieveDisplay> getCategoryIdentifier() {
        return PluginEntry.SIFTING;
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public int getDisplayWidth(SieveDisplay display) {
        return WIDTH;
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
    public List<Widget> setupDisplay(SieveDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();

        var outputChances = display.getOutputChances();

        var mesh = display.getMesh();
        var siftable = display.getBlocks().get(0);
        var fluid = display.getFluids().get(0);
        var outputs = display.getOutputEntries();

        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + ARROW_OFFSET_X, bounds.getMinY() + ARROW_OFFSET_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_U, ARROW_V));

        // Blocks
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + BLOCK_X, bounds.getMinY() + BLOCK_Y)).entries(siftable));
        // Meshes
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + MESH_X, bounds.getMinY() + MESH_Y)).entries(mesh));
        // Fluids
        if (!fluid.get(0).isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + FLUID_X, bounds.getMinY() + FLUID_Y)).entries(fluid));
        }
        // Outputs
        for (int y = 0; y < OUTPUT_SLOTS_Y; ++y) {
            for (int x = 0; x < OUTPUT_SLOTS_X; ++x) {
                var slot = Widgets.createSlot(new Point(bounds.getMinX() + OUTPUT_X + 18 * x, bounds.getMinY() + OUTPUT_Y + 18 * y));
                var index = y * OUTPUT_SLOTS_X + x;
                Widget widget = slot;
                if (index < outputs.size()) {
                    var output = outputs.get(index);
                    List<Text> tooltips = new ArrayList<>();
                    var chances = outputChances.get(output);
                    for (var chance : chances) {
                        if (chance <= 0) continue;
                        tooltips.add(Text.literal(chance * 100 + "%"));
                    }
                    
                    if (!tooltips.isEmpty()) {
                        slot.entries(output);
                        slot.disableTooltips();
                        widget = Widgets.withTooltip(slot, tooltips);
                    }
                    
                }
                widgets.add(widget);
            }
        }
        return widgets;
    }

}
