package wraith.fabricaeexnihilo.api.recipes;

import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.crafting.Lootable;

import java.util.ArrayList;
import java.util.List;

public record ToolRecipe(ItemIngredient ingredient, List<Lootable> lootables) {

    public ToolRecipe(ItemIngredient ingredient) {
        this(ingredient, new ArrayList<>());
    }

    public ToolRecipe(List<Lootable> lootables) {
        this(ItemIngredient.EMPTY, lootables);
    }

    public ToolRecipe() {
        this(ItemIngredient.EMPTY, new ArrayList<>());
    }

}
