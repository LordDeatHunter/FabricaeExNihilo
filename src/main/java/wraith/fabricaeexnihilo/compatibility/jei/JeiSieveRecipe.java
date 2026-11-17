package wraith.fabricaeexnihilo.compatibility.jei;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import java.util.List;

/**
 * A single JEI recipe page for sieving.
 *
 * <p>One page groups every output produced by one (input block, mesh, waterlogged)
 * combination, so that JEI's "uses" lookup on an item such as raw iron pieces
 * shows exactly which block and mesh produce it.
 */
public record JeiSieveRecipe(Ingredient input, ItemStack mesh, boolean waterlogged, List<Output> outputs) {

    public JeiSieveRecipe {
        outputs = List.copyOf(outputs);
    }

    /**
     * @param chances one probability per independent roll; each successful roll
     *                yields one extra copy of {@code stack}
     */
    public record Output(ItemStack stack, List<Double> chances) {
        public Output {
            chances = List.copyOf(chances);
        }
    }
}
