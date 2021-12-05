package wraith.fabricaeexnihilo.compatibility.rei.sieve;

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

public class SieveCategory implements DisplayCategory<SieveDisplay> {

    @Override
    public CategoryIdentifier<? extends SieveDisplay> getCategoryIdentifier() {
        return PluginEntry.SIEVE;
    }

    @Override
    public Renderer getIcon() {
        return ItemUtils.asREIEntry("oak_sieve");
    }

    @Override
    public Text getTitle() {
        return new TranslatableText("Sieve");
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
    public List<Widget> setupDisplay(SieveDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + ARROW_OFFSET_X, bounds.getMinY() + ARROW_OFFSET_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_U, ARROW_V));

        var inputs = display.getInputEntries();
        var outputs = display.getOutputEntries();

        // Sieves
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + MESH_X, bounds.getMinY() + MESH_Y)).entries(inputs.get(3)));
        // Meshes
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + MESH_X, bounds.getMinY() + MESH_Y)).entries(inputs.get(1)));
        // Fluids
        if (!inputs.get(2).isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + BUCKET_X, bounds.getMinY() + BUCKET_Y)).entries(inputs.get(2)));
        }
        // Sievables
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

    public static Identifier ARROW = FabricaeExNihilo.ID("textures/gui/rei/glyphs.png");

    public static int OUTPUT_SLOTS_X = Math.max(FabricaeExNihilo.CONFIG.modules.REI.sieveNumCols, 1);
    public static int OUTPUT_SLOTS_Y = Math.max(FabricaeExNihilo.CONFIG.modules.REI.sieveNumRows, 3);

    public static int MARGIN = 6;

    public static int WIDTH = 2 * 18 + OUTPUT_SLOTS_X * 18 + MARGIN * 2;
    public static int HEIGHT = OUTPUT_SLOTS_Y * 18 + MARGIN * 2;

    public static int BLOCK_X = MARGIN;
    public static int MESH_X = MARGIN;
    public static int BUCKET_X = MARGIN;
    public static int ARROW_OFFSET_X = MESH_X + 18;
    public static int OUTPUT_X = ARROW_OFFSET_X + 18;

    public static int BLOCK_Y = MARGIN + (HEIGHT - 2 * MARGIN) / 2 - 27;
    public static int MESH_Y = BLOCK_Y + 18;
    public static int BUCKET_Y = MESH_Y + 18;
    public static int ARROW_OFFSET_Y = MESH_Y;
    public static int OUTPUT_Y = MARGIN;

    public static int ARROW_WIDTH = 16;
    public static int ARROW_HEIGHT = 16;
    public static int ARROW_U = 0;
    public static int ARROW_V = 0;

    public static int MAX_OUTPUTS = OUTPUT_SLOTS_X * OUTPUT_SLOTS_Y;

}
