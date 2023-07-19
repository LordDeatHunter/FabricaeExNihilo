package wraith.fabricaeexnihilo.compatibility.emi;

import dev.emi.emi.api.render.EmiTexture;
import net.minecraft.util.Identifier;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class FENEmiTextures {
    private static final Identifier GLYPHS = id("textures/gui/rei/glyphs.png");
    public static final EmiTexture HAMMER = new EmiTexture(GLYPHS, 16, 0, 16, 16);
    public static final EmiTexture CROOK = new EmiTexture(GLYPHS, 32, 0, 16, 16);
    public static final EmiTexture PLUS = new EmiTexture(GLYPHS, 48, 0, 16, 16);
    public static final EmiTexture ARROW_RIGHT = new EmiTexture(GLYPHS, 0, 0, 16, 16);
    public static final EmiTexture ARROW_LEFT = new EmiTexture(GLYPHS, 16, 0, 16, 16);
    public static final EmiTexture ARROW_DOWN = new EmiTexture(GLYPHS, 32, 0, 16, 16);
    public static final EmiTexture ARROW_UP = new EmiTexture(GLYPHS, 48, 0, 16, 16);

    private FENEmiTextures() {}
}
