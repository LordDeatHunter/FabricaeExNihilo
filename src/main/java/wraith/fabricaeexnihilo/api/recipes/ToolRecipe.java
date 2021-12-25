package wraith.fabricaeexnihilo.api.recipes;

import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.crafting.Loot;

import java.util.ArrayList;
import java.util.List;

public record ToolRecipe(ItemIngredient ingredient, List<Loot> loots) {

    public ToolRecipe(ItemIngredient ingredient) {
        this(ingredient, new ArrayList<>());
    }

    public ToolRecipe(List<Loot> loots) {
        this(ItemIngredient.EMPTY, loots);
    }

    public ToolRecipe() {
        this(ItemIngredient.EMPTY, new ArrayList<>());
    }

}
