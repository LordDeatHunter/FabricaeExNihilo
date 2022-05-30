package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;

import java.util.ArrayList;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

@SuppressWarnings("UnstableApiUsage")
public class LeakingCategory implements DisplayCategory<LeakingDisplay> {

    public static final Identifier ARROW = id("textures/gui/rei/glyphs.png");
    public static final int ARROW_HEIGHT = 16;
    public static final int ARROW_U = 0;
    public static final int ARROW_V = 0;
    public static final int ARROW_WIDTH = 16;
    public static final int MARGIN = 6;
    public static final int HEIGHT = 3 * 18 + MARGIN * 2;
    public static final int ARROW_OFFSET_Y = HEIGHT / 2 - 9;
    public static final int TARGET_Y = ARROW_OFFSET_Y + 9;
    public static final int BUCKET_Y = ARROW_OFFSET_Y - 9;
    public static final int OUTPUT_Y = ARROW_OFFSET_Y + 9;
    public static final int WIDTH = 8 * 18 + MARGIN * 2;
    // Center the arrow
    public static final int ARROW_OFFSET_X = WIDTH / 2 - 9;
    public static final int TARGET_X = ARROW_OFFSET_X - 18;
    public static final int BUCKET_X = ARROW_OFFSET_X - 18;
    public static final int OUTPUT_X = ARROW_OFFSET_X + 18;
    private final ItemStack icon;
    private final String name;

    public LeakingCategory(ItemStack icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    @Override
    public CategoryIdentifier<? extends LeakingDisplay> getCategoryIdentifier() {
        return PluginEntry.LEAKING;
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public int getDisplayWidth(LeakingDisplay display) {
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
    public List<Widget> setupDisplay(LeakingDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + ARROW_OFFSET_X, bounds.getMinY() + ARROW_OFFSET_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW, ARROW_U, ARROW_V));

        var block = display.getBlock().get(0);
        var fluid = display.getFluid().get(0);
        var loss = display.getAmount();
        var result = display.getOutputEntries();

        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + OUTPUT_X, bounds.getMinY() + OUTPUT_Y)).entries(result.get(0)));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + BUCKET_X, bounds.getMinY() + BUCKET_Y)).entries(fluid));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + TARGET_X, bounds.getMinY() + TARGET_Y)).entries(block));

        var bucketStack = fluid.stream().findFirst().orElse(null);
        String label = "?";

        if (bucketStack != null && bucketStack.getValue() instanceof ItemStack stack && StorageUtil.findExtractableResource(FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack)), null) != null) {
            label = String.valueOf(loss);
        }

        var text = Widgets.createLabel(new Point(0, 0), new LiteralText("-" + label));
        text.setPoint(new Point(bounds.getMaxX() - MARGIN - text.getBounds().getMaxX(), bounds.getMinY() + MARGIN + 9));
        widgets.add(text);

        return widgets;
    }

}