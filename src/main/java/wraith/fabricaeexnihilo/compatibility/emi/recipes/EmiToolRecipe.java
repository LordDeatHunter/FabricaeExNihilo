package wraith.fabricaeexnihilo.compatibility.emi.recipes;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.emi.EmiIngredientUtil;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiPlugin;
import wraith.fabricaeexnihilo.recipe.ToolRecipe;

import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class EmiToolRecipe extends BasicEmiRecipe {
    private static final int WIDTH = 90;
    private static final int HEIGHT = 30;
    private static final int CENTER_X = WIDTH / 2;
    private static final int CENTER_Y = HEIGHT / 2;
    private static final int OFFSET = 16;

    private static final Identifier GLYPHS = id("textures/gui/rei/glyphs.png");
    private final EmiIngredient block;
    private final EmiStack result;
    private final int glyphU;

    public EmiToolRecipe(ToolRecipe recipe) {
        super(recipe.getTool() == ToolRecipe.ToolType.HAMMER ? FENEmiPlugin.HAMMERING_CATEGORY : FENEmiPlugin.CROOKING_CATEGORY,
                recipe.getId(), WIDTH, HEIGHT);
        block = EmiIngredientUtil.ingredientOf(recipe.getBlock());
        result = EmiStack.of(recipe.getResult().stack()).setChance((float) recipe.getResult().chances().stream().mapToDouble(Double::doubleValue).sum());
        inputs.add(block);
        outputs.add(result);
        glyphU = (recipe.getTool() == ToolRecipe.ToolType.HAMMER) ? 16 : 32;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(block, CENTER_X - OFFSET - 18, CENTER_Y - 9);
        widgets.addTexture(GLYPHS, CENTER_X - 8, CENTER_Y - 8, 16, 16, glyphU, 0);
        widgets.addSlot(result, CENTER_X + OFFSET, CENTER_Y - 13).large(true).recipeContext(this);
    }
}
