package wraith.fabricaeexnihilo.compatibility.rei.sieve;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.recipe.SieveRecipe;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record SieveRecipeOutputs(Map<EntryIngredient, List<Double>> outputs) {
    public static SieveRecipeOutputs of(SieveRecipe recipe, Identifier mesh) {
        return new SieveRecipeOutputs(new HashMap<>(Map.of(EntryIngredients.of(recipe.getResult()), recipe.getRolls().get(mesh))));
    }

    public List<SieveRecipeOutputs> split(int targetSize) {
        var results = new ArrayList<SieveRecipeOutputs>();
        var outputs = new HashMap<EntryIngredient, List<Double>>();

        int currentSize = 0;
        for (Map.Entry<EntryIngredient, List<Double>> entry : this.outputs.entrySet()) {
            outputs.put(entry.getKey(), entry.getValue());
            if (currentSize >= targetSize) {
                results.add(new SieveRecipeOutputs(outputs));
                outputs = new HashMap<>();
                currentSize = 0;
            } else {
                currentSize++;
            }
        }
        if (!outputs.isEmpty()) results.add(new SieveRecipeOutputs(outputs));


        return results;
    }
}
