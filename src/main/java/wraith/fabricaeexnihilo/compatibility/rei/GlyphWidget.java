package wraith.fabricaeexnihilo.compatibility.rei;

import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.WidgetWithBounds;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class GlyphWidget extends WidgetWithBounds {
    private final int width;
    private final int height;
    private final int x;
    private final int y;
    private final Rectangle bounds;
    private final Identifier texture;
    private final int u;
    private final int v;

    public GlyphWidget(Rectangle bounds, int x, int y, int width, int height, Identifier texture, int u, int v) {
        this.bounds = new Rectangle(bounds);
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(texture, x, y, u, v, width, height);
    }

    @Override
    public List<? extends Element> children() {
        return Collections.emptyList();
    }
}
