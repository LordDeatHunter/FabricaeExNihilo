package wraith.fabricaeexnihilo.compatibility.rei.sieve;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;

import java.util.ArrayList;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class SieveCategory implements DisplayCategory<SieveDisplay> {

    public static Identifier ARROW = id("textures/gui/rei/glyphs.png");
    public static int ARROW_HEIGHT = 16;
    public static int ARROW_U = 0;
    public static int ARROW_V = 0;
    public static int ARROW_WIDTH = 16;
    public static int MARGIN = 6;
    public static int HEIGHT = 4 * 18 + MARGIN * 2;
    public static int SIFT_Y = MARGIN + (HEIGHT - 2 * MARGIN) / 2 - 27;
    public static int BLOCK_Y = SIFT_Y + 18;
    public static int MESH_Y = BLOCK_Y + 18;
    public static int BUCKET_Y = MESH_Y + 18;
    public static int ARROW_OFFSET_Y = MESH_Y;
    public static int BLOCK_X = MARGIN;
    public static int BUCKET_X = MARGIN;
    public static int MESH_X = MARGIN;
    public static int ARROW_OFFSET_X = MESH_X + 18;
    public static int OUTPUT_X = ARROW_OFFSET_X + 18;
    public static int OUTPUT_SLOTS_X = Math.max(FabricaeExNihilo.CONFIG.modules.REI.sieveNumCols, 1);
    public static int OUTPUT_SLOTS_Y = Math.max(FabricaeExNihilo.CONFIG.modules.REI.sieveNumRows, 3);
    public static int MAX_OUTPUTS = OUTPUT_SLOTS_X * OUTPUT_SLOTS_Y;
    public static int OUTPUT_Y = MARGIN;
    public static int SIFT_X = MARGIN;
    public static int WIDTH = 2 * 18 + OUTPUT_SLOTS_X * 18 + MARGIN * 2;
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
        return new TranslatableText(this.name);
    }

    @Override
    public List<Widget> setupDisplay(SieveDisplay display, Rectangle bounds) {
        // TODO: Actually implement this properly
        var widgets = new ArrayList<Widget>();

        var meshEntry = display.getChancesForMeshes().entrySet().stream().findFirst().orElse(null);
        if (meshEntry == null) {
            return widgets;
        }
        var mesh = Registry.ITEM.get(meshEntry.getKey());
        var siftable = display.getBlock().get(0);
        var fluid = display.getFluid().get(0);
        var outputs = display.getOutputEntries().get(0);

        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + ARROW_OFFSET_X, bounds.getMinY() + ARROW_OFFSET_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_U, ARROW_V));

        // Sieves
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + SIFT_X, bounds.getMinY() + SIFT_Y)).entries(siftable));
        // Meshes
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + MESH_X, bounds.getMinY() + MESH_Y)).entries(EntryIngredients.of(mesh)));
        // Fluids
        if (!fluid.isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + BUCKET_X, bounds.getMinY() + BUCKET_Y)).entries(fluid));
        }
        // Sievables
        var slot = Widgets.createSlot(new Point(bounds.getMinX() + BLOCK_X, bounds.getMinY() + BLOCK_Y)).entries(outputs);
        var tooltip = new LiteralText("");
        for (var chance : meshEntry.getValue()) {
            if (chance <= 0) {
                continue;
            }
            tooltip.append(chance * 100 + "%");
        }
        Widgets.withTooltip(slot, tooltip);
        widgets.add(slot);

        return widgets;
    }

}
