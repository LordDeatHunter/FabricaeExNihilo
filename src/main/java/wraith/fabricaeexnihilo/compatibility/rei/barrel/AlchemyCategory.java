package wraith.fabricaeexnihilo.compatibility.rei.barrel;

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
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;

import java.util.ArrayList;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class AlchemyCategory implements DisplayCategory<AlchemyDisplay> {

    public static final int ARROW_U = 0;
    public static final int ARROW_V = 0;
    public static final Identifier GLYPHS = id("textures/gui/rei/glyphs.png");
    public static final int MARGIN = 6;
    public static final int HEIGHT = 3 * 18 + MARGIN * 2;
    public static final int BARRELS_Y = HEIGHT / 2;
    public static final int ARROW_Y = BARRELS_Y - 18;
    public static final int REACTANT_Y = ARROW_Y;
    public static final int PLUS_Y = REACTANT_Y;
    public static final int CATALYST_Y = PLUS_Y;
    public static final int PRODUCT_Y = ARROW_Y;
    public static final int BYPRODUCT_Y = PRODUCT_Y;
    public static final int SPAWN_Y = BYPRODUCT_Y;
    public static final int PLUS_U = 3 * 16;
    public static final int PLUS_V = 0;
    public static final int WIDTH = 8 * 18 + MARGIN * 2;
    public static final int BARRELS_X = WIDTH / 2 - 9;
    public static final int ARROW_X = BARRELS_X;
    public static final int REACTANT_X = ARROW_X - 18;
    public static final int PLUS_X = REACTANT_X - 18;
    public static final int CATALYST_X = PLUS_X - 18;
    public static final int PRODUCT_X = ARROW_X + 18;
    public static final int BYPRODUCT_X = PRODUCT_X + 18;
    public static final int SPAWN_X = BYPRODUCT_X + 18;
    private final ItemStack icon;
    private final String name;

    public AlchemyCategory(ItemStack icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    @Override
    public CategoryIdentifier<? extends AlchemyDisplay> getCategoryIdentifier() {
        return PluginEntry.ALCHEMY;
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public int getDisplayWidth(AlchemyDisplay display) {
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
    public List<Widget> setupDisplay(AlchemyDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));

        // Plus Glyph
        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + PLUS_X, bounds.getMinY() + PLUS_Y, 16, 16, GLYPHS, PLUS_U, PLUS_V));
        // Arrow Glyph
        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + ARROW_X, bounds.getMinY() + ARROW_Y, 16, 16, GLYPHS, ARROW_U, ARROW_V));

        var outputs = display.getOutputEntries();
        var catalyst = display.getCatalyst().get(0);
        var reactant = display.getReactant().get(0);

        var barrels = display.getBarrel();
        var product = outputs.get(0);
        var byproduct = outputs.get(1);
        var toSpawn = outputs.get(2);

        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + REACTANT_X, bounds.getMinY() + REACTANT_Y)).entries(reactant));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + CATALYST_X, bounds.getMinY() + CATALYST_Y)).entries(catalyst));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + PRODUCT_X, bounds.getMinY() + PRODUCT_Y)).entries(product));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + BYPRODUCT_X, bounds.getMinY() + BYPRODUCT_Y)).entries(byproduct));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + SPAWN_X, bounds.getMinY() + SPAWN_Y)).entries(toSpawn));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + BARRELS_X, bounds.getMinY() + BARRELS_Y)).entries(barrels));

        return widgets;
    }

}