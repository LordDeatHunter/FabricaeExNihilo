package wraith.fabricaeexnihilo.compatibility.emi.recipes;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.emi.EmiIngredientUtil;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiPlugin;
import wraith.fabricaeexnihilo.recipe.ToolRecipe;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class EmiToolRecipe extends BasicEmiRecipe {
    private static final int HEIGHT = 18;

    private static final Identifier GLYPHS = id("textures/gui/rei/glyphs.png");
    private final EmiIngredient block;
    private final int glyphU;

    public EmiToolRecipe(ToolRecipe recipe) {
        super(recipe.getTool() == ToolRecipe.ToolType.HAMMER ? FENEmiPlugin.HAMMERING_CATEGORY : FENEmiPlugin.CROOKING_CATEGORY,
                recipe.getId(), calcWidth(recipe), HEIGHT);
        block = EmiIngredientUtil.ingredientOf(recipe.getBlock());
        inputs.add(block);
        outputs.addAll(EmiIngredientUtil.stacksOf(recipe.getResult()));
        glyphU = (recipe.getTool() == ToolRecipe.ToolType.HAMMER) ? 16 : 32;
    }

    private static int calcWidth(ToolRecipe recipe) {
        return 18 * 2 + recipe.getResult().chances().size() * 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(block, 0, 0);
        widgets.addTexture(GLYPHS, 18 + 1, 1, 16, 16, glyphU, 0);
        for (int i = 0; i < outputs.size(); i++) {
            widgets.addSlot(outputs.get(i), 18 * (2 + i), 0).recipeContext(this);
        }
    }
}
