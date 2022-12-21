package wraith.fabricaeexnihilo.compatibility.rei.sieve;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.recipe.SieveRecipe;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.*;

public record SieveRecipeHolder(EntryIngredient input, boolean waterlogged, EntryIngredient mesh,
                                Map<EntryIngredient, List<Double>> outputs) {

    public static List<SieveRecipeHolder> fromRecipe(SieveRecipe recipe) {
        var holders = new ArrayList<SieveRecipeHolder>();

        var output = EntryIngredients.of(recipe.getResult());

        for (var entry : recipe.getRolls().entrySet()) {
            holders.add(new SieveRecipeHolder(EntryIngredients.ofIngredient(recipe.getInput()),
                    recipe.isWaterlogged(),
                    EntryIngredients.of(ItemUtils.getItem(entry.getKey())),
                    new HashMap<>(Map.of(output, entry.getValue()))));
        }

        return holders;
    }

    public void add(SieveRecipeHolder recipe) {
        this.outputs.putAll(recipe.outputs);
    }

    public List<SieveRecipeHolder> split(int size) {
        var holders = new ArrayList<SieveRecipeHolder>();
        var outputs = new HashMap<EntryIngredient, List<Double>>();

        int i = 0;
        for (var iterator = this.outputs.entrySet().iterator(); iterator.hasNext(); ) {
            var entry = iterator.next();

            outputs.put(entry.getKey(), entry.getValue());
            if (i >= size || !iterator.hasNext()) {
                holders.add(new SieveRecipeHolder(input, waterlogged, mesh, outputs));
                outputs = new HashMap<>();
                i = 0;
            } else {
                ++i;
            }
        }

        return holders;
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, waterlogged, mesh);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SieveRecipeHolder that = (SieveRecipeHolder) o;

        if (waterlogged != that.waterlogged) return false;
        if (!input.equals(that.input)) return false;
        return mesh.equals(that.mesh);
    }
}
