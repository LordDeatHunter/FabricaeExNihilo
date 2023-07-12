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
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.compatibility.recipeviewer.EntityRenderer;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;

import java.util.ArrayList;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class WitchWaterEntityCategory implements DisplayCategory<WitchWaterEntityDisplay> {

    public static final Identifier ARROW = id("textures/gui/rei/glyphs.png");
    public static final int ARROW_IN_U = 0;
    public static final int ARROW_IN_V = 16 * 5;
    public static final int ARROW_OUT_U = 0;
    public static final int ARROW_OUT_V = 16 * 4;
    public static final int HEIGHT = 3 * 18 + 6 * 4;
    public static final int WIDTH = 8 * 18 + 6 * 2;

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
        return Text.translatable("fabricaeexnihilo.rei.category.witch_water.entity");
    }

    @Override
    public List<Widget> setupDisplay(WitchWaterEntityDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();

        Entity target = display.target.create(MinecraftClient.getInstance().world);
        Entity result = display.result.create(MinecraftClient.getInstance().world);

        if (target == null || result == null) {
            FabricaeExNihilo.LOGGER.warn("Unable to create REI display entity");
            return widgets;
        }

        widgets.add(Widgets.createRecipeBase(bounds));
        Rectangle targetBounds = new Rectangle(bounds.getMinX(), bounds.getCenterY() - 27, 54, 54);
        Rectangle resultBounds = new Rectangle(bounds.getMaxX() - 55, bounds.getCenterY() - 27, 54, 54);
        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + 60, bounds.getMinY() + 20, 16, 16, ARROW, ARROW_IN_U, ARROW_IN_V));
        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + 80, bounds.getMinY() + 20, 16, 16, ARROW, ARROW_OUT_U, ARROW_OUT_V));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 70, bounds.getMinY() + 42)).entries(EntryIngredients.of(WitchWaterFluid.BUCKET)));

        var targetTooltip = new ArrayList<>(List.of(target.getDisplayName()));
        var nbt = display.nbt.toString();
        if (!nbt.equals("{}")) {
            targetTooltip.add(Text.translatable("emi.category.fabricaeexnihilo.witch_water_entity.nbt_required").formatted(Formatting.GRAY));
            targetTooltip.add(Text.literal("  ").append(Text.literal(nbt).formatted(Formatting.YELLOW)));
        }
        widgets.add(Widgets.createTooltip(targetBounds, targetTooltip));
        widgets.add(Widgets.createTooltip(resultBounds, result.getDisplayName()));
        widgets.add(Widgets.createDrawableWidget((helper, mouseX, mouseY, delta) -> EntityRenderer.drawEntity(helper, mouseX, mouseY, target, targetBounds.getMinX(), targetBounds.getMinY(), targetBounds.getMaxX(), targetBounds.getMaxY(), 32)));
        widgets.add(Widgets.createDrawableWidget((helper, mouseX, mouseY, delta) -> EntityRenderer.drawEntity(helper, mouseX, mouseY, result, resultBounds.getMinX(), resultBounds.getMinY(), resultBounds.getMaxX(), resultBounds.getMaxY(), 32)));

        return widgets;
    }

}