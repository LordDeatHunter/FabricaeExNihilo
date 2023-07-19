package wraith.fabricaeexnihilo.compatibility.emi.recipes;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.fluid.Fluids;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiPlugin;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiTextures;
import wraith.fabricaeexnihilo.compatibility.recipeviewer.SieveRecipeKey;
import wraith.fabricaeexnihilo.compatibility.recipeviewer.SieveRecipeOutputs;

public class EmiSieveRecipe extends BasicEmiRecipe {
    private static final int WIDTH = 11 * 18;
    private static final int HEIGHT = 3 * 18;
    private final SieveRecipeKey key;

    public EmiSieveRecipe(SieveRecipeKey key, SieveRecipeOutputs outputs) {
        super(FENEmiPlugin.SIEVE_CATEGORY, null, WIDTH, HEIGHT);
        inputs.add(EmiIngredient.of(key.input()));
        catalysts.add(EmiStack.of(key.mesh()));
        if (key.waterlogged()) catalysts.add(EmiStack.of(Fluids.WATER));
        this.key = key;

        outputs.outputs().forEach((stack, chance) -> this.outputs.add(EmiStack.of(stack).setChance((float)(double)chance)));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(EmiIngredient.of(key.input()), 0, 0);
        widgets.addSlot(EmiStack.of(key.mesh()), 0, 18).catalyst(true);
        if (key.waterlogged())
            widgets.addSlot(EmiStack.of(Fluids.WATER), 0, 36).catalyst(true);

        widgets.addTexture(FENEmiTextures.ARROW_RIGHT, 18, 18);

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 3; y++) {
                var slotIndex = y * 9 + x;
                if (slotIndex < outputs.size()) {
                    widgets.addSlot(outputs.get(slotIndex), 36 + x * 18, y * 18).recipeContext(this);
                } else {
                    widgets.addSlot(36 + x * 18, y * 18);
                }
            }
        }
    }
}
