package wraith.fabricaeexnihilo.compatibility.rei.witchwater;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;

import java.util.ArrayList;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class WitchWaterEntityCategory implements DisplayCategory<WitchWaterEntityDisplay> {

    public static final Identifier ARROW = id("textures/gui/rei/glyphs.png");
    public static final int ARROW_HEIGHT = 16;
    public static final int ARROW_IN_U = 0;
    public static final int ARROW_IN_V = 16 * 5;
    public static final int ARROW_OUT_U = 0;
    public static final int ARROW_OUT_V = 16 * 4;
    public static final int ARROW_WIDTH = 16;
    public static final int MARGIN = 6;
    public static final int HEIGHT = 3 * 18 + MARGIN * 2 - 9;
    public static final int FLUID_Y = HEIGHT / 2 + 9;
    public static final int ARROW_IN_Y = FLUID_Y - 18;
    public static final int IN_Y = ARROW_IN_Y - 7;
    public static final int ARROW_OUT_Y = FLUID_Y - 18;
    public static final int OUT_Y = ARROW_OUT_Y;
    public static final int WIDTH = 8 * 18 + MARGIN * 2;
    public static final int FLUID_X = WIDTH / 2 - 9;
    public static final int ARROW_IN_X = FLUID_X - 5;
    public static final int IN_X = ARROW_IN_X - 18;
    public static final int ARROW_OUT_X = FLUID_X + 9;
    public static final int OUT_X = ARROW_OUT_X + 18;

    @Override
    public CategoryIdentifier<? extends WitchWaterEntityDisplay> getCategoryIdentifier() {
        return PluginEntry.WITCH_WATER_ENTITY;
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public int getDisplayWidth(WitchWaterEntityDisplay display) {
        return WIDTH;
    }

    public Renderer getIcon() {
        return EntryStacks.of(WitchWaterFluid.BUCKET);
    }

    @Override
    public Text getTitle() {
        return new TranslatableText("fabricaeexnihilo.rei.category.witch_water.entity");
    }

    @Override
    public List<Widget> setupDisplay(WitchWaterEntityDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));

        var arrowIn = new GlyphWidget(bounds, bounds.getMinX() + ARROW_IN_X, bounds.getMinY() + ARROW_IN_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_IN_U, ARROW_IN_V);
        var arrowOut = new GlyphWidget(bounds, bounds.getMinX() + ARROW_OUT_X, bounds.getMinY() + ARROW_OUT_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_OUT_U, ARROW_OUT_V);
        widgets.add(arrowIn);
        widgets.add(arrowOut);

        var eggsIn = display.getInputEntries().get(0);
        var eggOut = display.getOutputEntries().get(0);
        var fluids = EntryIngredients.of(WitchWaterFluid.BUCKET);

        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + IN_X, bounds.getMinY() + IN_Y)).entries(eggsIn));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + OUT_X, bounds.getMinY() + OUT_Y)).entries(eggOut));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + FLUID_X, bounds.getMinY() + FLUID_Y)).entries(fluids));

        if (display.getTarget().test(EntityType.VILLAGER)) {
            var profession = display.getProfession();
            var professionId = Registry.VILLAGER_PROFESSION.getId(profession == null ? VillagerProfession.NONE : profession);
            var text = Widgets.createLabel(new Point(0, 0), new TranslatableText("entity." + professionId.getNamespace() + ".villager." + professionId.getPath()));
            text.setPoint(new Point(bounds.getMinX() + MARGIN + text.getBounds().getMaxX(), bounds.getMinY() - MARGIN + text.getBounds().getMaxY()));
            widgets.add(text);
        }

        return widgets;
    }

}