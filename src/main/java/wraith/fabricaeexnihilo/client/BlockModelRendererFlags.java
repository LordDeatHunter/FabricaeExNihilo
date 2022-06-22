package wraith.fabricaeexnihilo.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class BlockModelRendererFlags {
    private BlockModelRendererFlags() {
    }
    
    private static boolean colorOverride = false;
    
    public static void setColorOverride(boolean enabled) {
        colorOverride = enabled;
    }
    
    public static boolean isColorOverridden() {
        return colorOverride;
    }
}
