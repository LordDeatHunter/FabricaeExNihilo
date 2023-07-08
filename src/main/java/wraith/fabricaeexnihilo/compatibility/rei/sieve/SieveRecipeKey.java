package wraith.fabricaeexnihilo.compatibility.rei.sieve;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.recipe.SieveRecipe;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public record SieveRecipeKey(EntryIngredient input, boolean waterlogged, EntryIngredient mesh, Identifier meshKey) {
    public static List<SieveRecipeKey> getKeys(SieveRecipe recipe) {
        var keys = new ArrayList<SieveRecipeKey>();

        for (var entry : recipe.getRolls().entrySet()) {
            keys.add(new SieveRecipeKey(EntryIngredients.ofIngredient(recipe.getInput()),
                    recipe.isWaterlogged(),
                    EntryIngredients.of(ItemUtils.getItem(entry.getKey())),
                    entry.getKey()));
        }

        return keys;
    }
}
