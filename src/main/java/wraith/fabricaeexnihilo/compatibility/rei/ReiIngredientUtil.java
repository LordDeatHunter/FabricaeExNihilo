package wraith.fabricaeexnihilo.compatibility.rei;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;

public class ReiIngredientUtil {
    private ReiIngredientUtil() {}

    public static EntryIngredient of(FluidIngredient fluid) {
        return fluid.getValue().map(EntryIngredients::of, EntryIngredients::ofFluidTag);
    }

    public static EntryIngredient of(BlockIngredient fluid) {
        return fluid.getValue().map(EntryIngredients::of, tag -> EntryIngredients.ofTag(tag, entry -> EntryStacks.of(entry.value())));
    }
}
