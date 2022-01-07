package wraith.fabricaeexnihilo.modules.base;

import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.FabricaeExNihilo;

public class FluidSettings {
    private final Identifier flowingTexture;
    private final Identifier stillTexture;
    private final boolean infinite;
    private final String basePath;
    
    public FluidSettings(String basePath, boolean infinite) {
        this.basePath = basePath;
        this.infinite = infinite;
        this.flowingTexture = FabricaeExNihilo.id("block/fluid/" + basePath + "_flow");
        this.stillTexture = FabricaeExNihilo.id("block/fluid/" + basePath + "_still");
    }
    
    public Identifier getFlowingTexture() {
        return flowingTexture;
    }
    
    public Identifier getStillTexture() {
        return stillTexture;
    }
    
    public String getBasePath() {
        return basePath;
    }
    
    public boolean isInfinite() {
        return infinite;
    }
}
