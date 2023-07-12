package wraith.fabricaeexnihilo.compatibility.recipeviewer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.recipe.SieveRecipe;

import java.util.ArrayList;
import java.util.List;

public record SieveRecipeOutputs(HashMultimap<ItemStack, List<Double>> outputs) {
    public static SieveRecipeOutputs of(SieveRecipe recipe, Identifier mesh) {
        return new SieveRecipeOutputs(HashMultimap.create(ImmutableMultimap.of(recipe.getResult(), recipe.getRolls().get(mesh))));
    }

    public List<SieveRecipeOutputs> split(int targetSize) {
        var results = new ArrayList<SieveRecipeOutputs>();
        var outputs = HashMultimap.<ItemStack, List<Double>>create();

        int currentSize = 0;
        for (var entry : this.outputs.entries()) {
            outputs.put(entry.getKey(), entry.getValue());
            if (currentSize >= targetSize) {
                results.add(new SieveRecipeOutputs(outputs));
                outputs = HashMultimap.create();
                currentSize = 0;
            } else {
                currentSize++;
            }
        }
        if (!outputs.isEmpty()) results.add(new SieveRecipeOutputs(outputs));

        return results;
    }
}
