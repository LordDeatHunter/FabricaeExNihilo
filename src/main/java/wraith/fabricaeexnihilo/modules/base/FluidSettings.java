package wraith.fabricaeexnihilo.modules.base;

import net.minecraft.util.Identifier;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class FluidSettings {
    private final String basePath;
    private final Identifier flowingTexture;
    private final boolean infinite;
    private final Identifier overlayTexture;
    private final Identifier stillTexture;
    private final int tint;
    private final boolean translucent;

    public FluidSettings(String basePath, int tint, boolean infinite, boolean translucent) {
        this.basePath = basePath;
        this.tint = tint;
        this.infinite = infinite;
        this.translucent = translucent;
        this.flowingTexture = id("block/fluid/" + basePath + "_flow");
        this.stillTexture = id("block/fluid/" + basePath + "_still");
        this.overlayTexture = id("block/fluid/" + basePath + "_overlay");
    }

    public String getBasePath() {
        return basePath;
    }

    public Identifier getFlowingTexture() {
        return flowingTexture;
    }

    public Identifier getOverlayTexture() {
        return overlayTexture;
    }

    public Identifier getStillTexture() {
        return stillTexture;
    }

    public int getTint() {
        return tint;
    }

    public boolean isInfinite() {
        return infinite;
    }

    public boolean isTranslucent() {
        return translucent;
    }

}
