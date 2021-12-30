package wraith.fabricaeexnihilo.recipe;

import com.google.gson.JsonObject;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;

public interface BaseSerializer<T extends Recipe<?>> extends RecipeSerializer<T> {
    JsonObject write(T recipe);
}
