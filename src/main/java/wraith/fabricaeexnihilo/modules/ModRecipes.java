package wraith.fabricaeexnihilo.modules;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.recipe.barrel.CompostRecipe;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class ModRecipes {
    public static final ModRecipeType<CompostRecipe> COMPOST = new ModRecipeType<>(id("compost"));
    
    public static final RecipeSerializer<CompostRecipe> COMPOST_SERIALIZER = new CompostRecipe.Serializer();
    
    public static void register() {
        Registry.register(Registry.RECIPE_TYPE, COMPOST.id(), COMPOST);
        
        Registry.register(Registry.RECIPE_SERIALIZER, COMPOST.id(), COMPOST_SERIALIZER);
    }
    
    private static record ModRecipeType<T extends Recipe<?>>(Identifier id) implements RecipeType<T> {
        @Override
        public String toString() {
            return id.toString();
        }
    }
}
