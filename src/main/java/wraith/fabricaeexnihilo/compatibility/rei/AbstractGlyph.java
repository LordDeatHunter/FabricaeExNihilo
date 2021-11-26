package wraith.fabricaeexnihilo.compatibility.rei;


import me.shedaniel.rei.api.client.gui.widgets.WidgetWithBounds;

public abstract class AbstractGlyph extends WidgetWithBounds {
    public final int getX() {
        return getBounds().getX();
    }

    public final int getY() {
        return getBounds().getY();
    }

    public abstract double getAnimationDuration();

    public abstract void setAnimationDuration(double animationDurationMS);

    public final AbstractGlyph animationDurationMS(double animationDurationMS) {
        setAnimationDuration(animationDurationMS);
        return this;
    }

    public final AbstractGlyph animationDurationTicks(double animationDurationTicks) {
        return animationDurationMS(animationDurationTicks * 50);
    }

    public final AbstractGlyph disableAnimation() {
        return animationDurationMS(-1);
    }

}
